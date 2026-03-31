package com.example.seeme.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.seeme.R
import com.example.seeme.activity.RegistrationActivity
import com.example.seeme.databinding.FragmentLoginBinding
import com.example.seeme.ui.LoginViewModel
import com.example.seeme.ui.fragments.country.item.CountryItem
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class LoginFragment : BaseRegistrationFragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel: LoginViewModel by viewModels()

    private var verificationId: String? = null
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.viewModel = loginViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUserInterface()
    }

    private fun initUserInterface() {
        setupCallbacks()
        initClickListener()
        observeViewModel()
        startObservingPhoneNumberChange()
    }

    private fun initClickListener() {
        binding.apply {
            constraintCountryCode.setOnClickListener {
                moveToCountrySelectScreen()
            }
            btnSendOtp.setOnClickListener {
                onNextClicked()
            }
        }
    }

    private fun moveToCountrySelectScreen() {
        hideKeyboard()
        val dialog = CountrySelectionDialogFragment(
            object : CountrySelectionInterface {
                override fun onCountryItemSelected(countryItem: CountryItem) {
                    updateCountrySelection(countryItem)
                }
            },
            binding.textCountryCode.text.toString().trim()
        )
        dialog.show(parentFragmentManager, "CountrySelectionDialog")
    }

    private fun updateCountrySelection(countryItem: CountryItem) {
        binding.textCountryCode.text = countryItem.dialCode
        
        // Dynamically load flag drawable
        val context = requireContext()
        val resourceId = context.resources.getIdentifier(
            "country_${countryItem.code.lowercase()}", 
            "drawable", 
            context.packageName
        )
        if (resourceId != 0) {
            binding.imageCountryIcon.setImageResource(resourceId)
        } else {
            binding.imageCountryIcon.setImageResource(android.R.drawable.ic_menu_help)
        }
    }

    private fun onNextClicked() {
        hideKeyboard()
        val phone = binding.editTextMobileNumber.text.toString().trim()
        if (phone.length == 10) {
            sendOtp(phone)
        } else {
            binding.editTextMobileNumber.error = getString(R.string.error_phone_number)
        }
    }

    private fun setupCallbacks() {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // Auto-verification or instant validation
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // Handle failure
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                verificationId = id
                navigateToOtpScreen()
            }
        }
    }

    private fun observeViewModel() {
        loginViewModel.onSendOtpClick = {
            onNextClicked()
        }
    }

    private fun startObservingPhoneNumberChange() {
        binding.editTextMobileNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun sendOtp(phone: String) {
        val countryCode = binding.textCountryCode.text.toString().trim()
        val fullPhoneNumber = "$countryCode$phone"

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(fullPhoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun navigateToOtpScreen() {
        val bundle = Bundle().apply {
            putString("verificationId", verificationId)
            putString("phoneNumber", binding.editTextMobileNumber.text.toString())
        }
        findNavController().navigate(R.id.action_loginFragment_to_otpFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
