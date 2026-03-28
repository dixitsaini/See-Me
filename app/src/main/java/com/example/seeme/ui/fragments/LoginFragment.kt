package com.example.seeme.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.seeme.R
import com.example.seeme.databinding.FragmentLoginBinding
import com.example.seeme.ui.LoginViewModel
import com.example.seeme.ui.fragments.country.item.CountryItem

import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class LoginFragment : BaseRegistrationFragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel: LoginViewModel by viewModels()

    private lateinit var firebaseAuth: FirebaseAuth
    private var verificationId: String? = null
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        setupCallbacks()
    }

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

        setupViewModelCallbacks()
        setupClickListeners()
        observeViewModel()
        setupCountryCodeSelection()
    }

    private fun setupCountryCodeSelection() {
        binding.llCountryCode.setOnClickListener {
            hideKeyboard()
            val bottomSheet = CountrySelectionBottomSheetDialogFragment(
                object : CountrySelectionInterface {
                    override fun onCountryItemSelected(countryItem: CountryItem) {
                        binding.tvCountryCode.text = countryItem.dialCode
                    }
                },
                binding.tvCountryCode.text.toString().trim()
            )
            bottomSheet.show(parentFragmentManager, "CountrySelectionBottomSheet")
        }
    }

    private fun setupCallbacks() {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // Handle error
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                this@LoginFragment.verificationId = verificationId
                navigateToOtpScreen()
            }
        }
    }

    private fun setupViewModelCallbacks() {
        loginViewModel.onSendOtpClick = {
            sendOtp()
        }
        loginViewModel.onBackClick = {
            requireActivity().onBackPressed()
        }
    }

    private fun setupClickListeners() {
        binding.btnSendOtp.setOnClickListener {
            loginViewModel.onSendOtpClick()
        }
        binding.btnBack.setOnClickListener {
            loginViewModel.onBackClick()
        }
    }

    private fun observeViewModel() {
        loginViewModel.isSendOtpEnabled.observe(viewLifecycleOwner, Observer { enabled ->
            binding.btnSendOtp.isEnabled = enabled
            binding.btnSendOtp.alpha = if (enabled) 1.0f else 0.5f
        })
    }

    private fun sendOtp() {
        val rawNumber = binding.etPhoneNumber.text?.toString()?.trim()?.replace(Regex("[^0-9]"), "") ?: ""

        if (rawNumber.length < 10) {
            binding.etPhoneNumber.error = getString(R.string.error_phone_number)
            return
        }

        val countryCode = binding.tvCountryCode.text?.toString()?.trim() ?: "+91"
        val formattedPhoneNumber = if (countryCode.startsWith("+")) "$countryCode$rawNumber" else "+$countryCode$rawNumber"

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(formattedPhoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
//        showLoading(getString(R.string.sending_otp))
    }

    private fun navigateToOtpScreen() {
        val bundle = Bundle().apply {
            putString("verificationId", verificationId)
        }
        findNavController().navigate(R.id.action_loginFragment_to_otpFragment, bundle)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Success logic
                } else {
                    // Error logic
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
