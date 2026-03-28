package com.example.seeme.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.seeme.R
import com.example.seeme.databinding.FragmentOtpBinding
import com.example.seeme.ui.OtpViewModel
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class OtpFragment : BaseRegistrationFragment() {

    private var _binding: FragmentOtpBinding? = null
    private val binding get() = _binding!!
    private val otpViewModel: OtpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get verification ID from arguments
        arguments?.getString("verificationId")?.let { verificationId ->
            otpViewModel.setVerificationId(verificationId)
        }
    }

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

        setupViewModelCallbacks()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupViewModelCallbacks() {
        otpViewModel.onVerifyOtpClick = {
            verifyOtp()
        }
        otpViewModel.onResendOtpClick = {
            resendOtp()
        }
        otpViewModel.onBackClick = {
            requireActivity().onBackPressed()
        }
    }

    private fun setupClickListeners() {
        binding.btnVerifyOtp.setOnClickListener {
            otpViewModel.onVerifyOtpClick()
        }
        binding.btnResendOtp.setOnClickListener {
            otpViewModel.onResendOtpClick()
        }
        binding.btnBack.setOnClickListener {
            otpViewModel.onBackClick()
        }
    }

    private fun observeViewModel() {
        otpViewModel.isVerifyOtpEnabled.observe(viewLifecycleOwner, Observer { enabled ->
            binding.btnVerifyOtp.isEnabled = enabled
            binding.btnVerifyOtp.alpha = if (enabled) 1.0f else 0.5f
        })
    }

    private fun verifyOtp() {
        val otpCode = otpViewModel.otp.value ?: return
        val verificationId = otpViewModel.verificationId.value ?: return

        val credential = PhoneAuthProvider.getCredential(verificationId, otpCode)
        signInWithPhoneAuthCredential(credential)
    }

    private fun resendOtp() {
        // Implement resend OTP logic
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    navigateToProfile()
                } else {
                    // Handle failure
                }
            }
    }

    private fun navigateToProfile() {
        // Navigate to profile screen
        findNavController().navigate(R.id.action_otpFragment_to_profileFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
