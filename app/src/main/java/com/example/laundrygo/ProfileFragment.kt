package com.example.laundrygo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer

class ProfileFragment : Fragment() {

    private val orderViewModel: OrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile2, container, false)

        // Initialize TextViews
        val tvOrderId: TextView = view.findViewById(R.id.tvOrderId)
        val tvFullName: TextView = view.findViewById(R.id.tvFullName)
        val tvContactNumber: TextView = view.findViewById(R.id.tvContactNumber)
        val tvAddress: TextView = view.findViewById(R.id.tvAddress)
        val tvDeliveryDate: TextView = view.findViewById(R.id.tvDeliveryDate)
        val tvDeliveryTime: TextView = view.findViewById(R.id.tvDeliveryTime)
        val tvPaymentMethod: TextView = view.findViewById(R.id.tvPaymentMethod)

        // Observe the orderDetails LiveData
        orderViewModel.orderDetails.observe(viewLifecycleOwner, Observer { orderDetails ->
            orderDetails?.let {
                Log.d("ProfileFragment", "Order details: $it")
                tvOrderId.text = it.orderId
                tvFullName.text = it.fullName
                tvContactNumber.text = it.contactNumber
                tvAddress.text = it.address
                tvDeliveryDate.text = it.deliveryDate
                tvDeliveryTime.text = it.deliveryTime
                tvPaymentMethod.text = it.paymentMethod
            }
        })



        return view
    }

}
