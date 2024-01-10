package com.thingclips.smart.bizbundle.devicedetail.demo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.thingclips.smart.archer.api.AbsBaseArcherCell
import com.thingclips.smart.archer.api.bean.IThingBaseData
import com.thingclips.smart.bizubundle.devicedetail.demo.R
import com.thingclips.smart.bizubundle.devicedetail.demo.databinding.DetailAddCellBinding

class CTestAsyncInsertCell : AbsBaseArcherCell() {

    lateinit var dataBinding: DetailAddCellBinding;
    var context: Context? = null
    var show = false;
    var text = "self control cell "
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

    }

    override fun onBindView(view: View): IThingBaseData? {
        dataBinding = DetailAddCellBinding.bind(view);
        dataBinding.menuListTitle.text = text
        dataBinding.llTitle.setOnClickListener {
            context?.apply {
                Toast.makeText(this, "action_test_insert", Toast.LENGTH_LONG).show()
                text = "self control cell change"
                getContainerControl().updateCell(this@CTestAsyncInsertCell)

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
            this@CTestAsyncInsertCell.context = context
        }
        val handler = Handler()
        handler.postDelayed({
            show = true
            getContainerControl().updateCell(this);
        }, (1000 * 5).toLong())
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
        return show;
    }
}