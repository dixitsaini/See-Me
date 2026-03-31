package com.example.seeme.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.seeme.R
import com.example.seeme.databinding.FragmentOtpBinding
import com.example.seeme.ui.OtpViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class OtpFragment : BaseRegistrationFragment() {

    private var _binding: FragmentOtpBinding? = null
    private val binding get() = _binding!!
    private val otpViewModel: OtpViewModel by viewModels()

    private var resendingToken: PhoneAuthProvider.ForceResendingToken? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOtpBinding.inflate(inflater, container, false)
        binding.viewModel = otpViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUserInterface()
    }

    private fun initUserInterface() {
        val verificationId = arguments?.getString("verificationId")
        val phoneNumber = arguments?.getString("phoneNumber") ?: ""

        if (verificationId != null) {
            otpViewModel.setVerificationId(verificationId)
        }

        binding.tvSubtitle.text = getString(R.string.otp_subtitle, phoneNumber)

        initClickListener()
        initBackButtonListener()
        observeViewModel()
        setupOtpTextWatcher()
    }

    private fun initClickListener() {
        binding.apply {
            btnVerifyOtp.setOnClickListener {
                verifyOtp()
            }
            btnResendOtp.setOnClickListener {
                resendOtp()
            }
            btnBack.setOnClickListener {
                handleBackNavigation()
            }
        }
    }

    private fun initBackButtonListener() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackNavigation()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun handleBackNavigation() {
        // Set flag in RegistrationViewModel if needed, as per model
//        viewModel.userRequestedChangeNumber = true
        findNavController().popBackStack()
    }

    private fun observeViewModel() {
        otpViewModel.onVerifyOtpClick = {
            verifyOtp()
        }
        otpViewModel.onResendOtpClick = {
            resendOtp()
        }
    }

    private fun setupOtpTextWatcher() {
        binding.etOtp.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 6) {
                    verifyOtp()
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun verifyOtp() {
        hideKeyboard()
        val otpCode = binding.etOtp.text.toString().trim()
        val verificationId = otpViewModel.verificationId.value

        if (otpCode.length == 6 && verificationId != null) {
            otpViewModel.setLoading(true)
            val credential = PhoneAuthProvider.getCredential(verificationId, otpCode)
            signInWithPhoneAuthCredential(credential)
        } else if (otpCode.length < 6) {
            Toast.makeText(requireContext(), "Please enter 6-digit OTP", Toast.LENGTH_SHORT).show()
        }
    }

    private fun resendOtp() {
        val phoneNumber = arguments?.getString("phoneNumber") ?: return
        val countryCode = "+91" // Ideally passed from LoginFragment
        val fullPhoneNumber = if (phoneNumber.startsWith("+")) phoneNumber else "$countryCode$phoneNumber"

        otpViewModel.setLoading(true)
        
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(fullPhoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(createResendCallbacks())

        resendingToken?.let {
            optionsBuilder.setForceResendingToken(it)
        }

        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }

    private fun createResendCallbacks() = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            otpViewModel.setLoading(false)
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            otpViewModel.setLoading(false)
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
        }

        override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
            otpViewModel.setLoading(false)
            otpViewModel.setVerificationId(id)
            resendingToken = token
            Toast.makeText(requireContext(), "OTP Resent", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                otpViewModel.setLoading(false)
                if (task.isSuccessful) {
                    saveUserAndProceed(task.result.user)
                } else {
                    Toast.makeText(requireContext(), "Invalid OTP", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
