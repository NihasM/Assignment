package com.noble.assignment.splash

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.noble.assignment.MainActivity
import com.noble.assignment.R
import com.noble.assignment.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setTitle("", toolbar = false)
        val rotateAnim = ObjectAnimator.ofFloat(binding.logo, "rotation", 0f, 360f).apply {
            duration = 1500
            interpolator = AccelerateDecelerateInterpolator()
        }

        val scaleX = ObjectAnimator.ofFloat(binding.logo, "scaleX", 1f, 0.3f).apply { duration = 1000 }
        val scaleY = ObjectAnimator.ofFloat(binding.logo, "scaleY", 1f, 0.3f).apply { duration = 1000 }

        val animatorSet = AnimatorSet().apply {
            playTogether(rotateAnim, scaleX, scaleY)
            start()
        }

        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator) {
                findNavController().navigate(R.id.action_splashFragment_to_userListFragment)
            }

            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Prevent memory leaks
    }
}
