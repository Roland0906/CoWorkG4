package app.appworks.school.stylish.coupon

import android.animation.Animator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import app.appworks.school.stylish.R
import app.appworks.school.stylish.databinding.FragmentCouponBinding
import app.appworks.school.stylish.login.UserManager


class CouponFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCouponBinding.inflate(inflater, container, false)

        val discountList = listOf("五折!你是怎麼抽到的?")

        var randomDiscount = ""

        binding.lottieAnimationView.setMinAndMaxProgress(0.3f, 0.8f)

        binding.buttonDraw.setOnClickListener {
            randomDiscount = discountList.random()
            UserManager.discount = randomDiscount
            binding.buttonUseCoupon.text = randomDiscount
            binding.lottieAnimationView.visibility = VISIBLE
            binding.lottieAnimationView.playAnimation()
            binding.buttonDraw.visibility = GONE

        }



        binding.buttonUseCoupon.setOnClickListener {

            Toast.makeText(context, "已使用優惠 $randomDiscount", Toast.LENGTH_SHORT).show()
            binding.layoutCoupon.visibility = GONE
        }

        binding.lottieAnimationView.addAnimatorListener(object :
            Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                binding.lottieAnimationView.visibility = VISIBLE
            }

            override fun onAnimationEnd(animation: Animator) {
                binding.lottieAnimationView.visibility = GONE
                binding.layoutCoupon.visibility = VISIBLE
            }

            override fun onAnimationCancel(animation: Animator) {
                TODO("Not yet implemented")
            }

            override fun onAnimationRepeat(animation: Animator) {
                TODO("Not yet implemented")
            }


        })



        return binding.root
    }


}