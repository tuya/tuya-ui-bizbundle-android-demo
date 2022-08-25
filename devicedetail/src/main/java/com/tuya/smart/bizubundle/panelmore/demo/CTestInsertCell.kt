package com.tuya.smart.bizubundle.panelmore.demo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tuya.smart.api.service.MicroServiceManager
import com.tuya.smart.archer.api.bean.ITYBaseData
import com.tuya.smart.archer.api.cell.IArcherCell
import com.tuya.smart.bizubundle.panelmore.demo.databinding.DetailAddCellBinding
import com.tuya.smart.device_detail.PluginDeviceDetailInfoService
import com.tuya.smart.device_detail.viewmodel.DeviceDetailViewModel
import com.tuya.smart.tuyasmart_device_detail.api.IPluginDeviceDetailInfoService

class CTestInsertCell : IArcherCell {

    lateinit var dataBinding: DetailAddCellBinding;
    var context: Context? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

    }

    override fun onBindView(view: View): ITYBaseData? {
        dataBinding = DetailAddCellBinding.bind(view);
        dataBinding.menuListTitle.text = "goto new Cell"
        dataBinding.llTitle.setOnClickListener {
            context?.apply {
                Toast.makeText(this, "action_test_insert", Toast.LENGTH_LONG).show()
            }

        }
        return null;
    }

    override fun onCreateView(parent: ViewGroup): View? {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.detail_add_cell, parent, false)
        return view
    }

    override fun onDestroy() {

    }

    override fun onPause() {

    }

    override fun onCreate(context: Context?) {
        context?.apply {
            this@CTestInsertCell.context = context
        }
        val service:IPluginDeviceDetailInfoService? = MicroServiceManager.getInstance()
            .findServiceByInterface(IPluginDeviceDetailInfoService::class.java.name)
        val deviceId = service?.getCurrentDevId();
        val groupId = service?.getCurrentGroupId();
        deviceId?.apply {

        }
        groupId?.apply {

        }


    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {

    }

    override fun onResume() {

    }

    override fun onSaveInstanceState(outState: Bundle) {

    }

    override fun onStop() {

    }

    override suspend fun show(): Boolean {
        return true;
    }
}