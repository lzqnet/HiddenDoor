package com.zql.hiddendoor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Toast
import com.zql.hitactions.Action
import com.zql.hitactions.HitActionLog
import com.zql.hitactions.HitActions
import com.zql.hitactions.IActionListener


class MainActivity : AppCompatActivity() {
    private val hitActions: HitActions = HitActions()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initHiddenDoor()
    }
    private fun initHiddenDoor() {
        hitActions.init(Looper.getMainLooper())
        val list = ArrayList<Action>()
        HidenDoorNumber.numberList.forEach {
            list.add(Action(it))
        }
        hitActions.setActionSequence(list)
        hitActions.setActionListener(object : IActionListener {
            override fun onActionStateChange(result: Boolean) {
                if (result) {
                    openDebuger()
                    Toast.makeText(applicationContext,"打开调试页面",Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext,"匹配失败",Toast.LENGTH_SHORT).show()

                }
            }

        })
        HitActionLog.setLogger(HitLogger())

    }

    private fun openDebuger() {
        val intent=Intent()
        intent.setClassName(packageName,"com.zql.hiddendoor.Debugger")
        startActivity(intent)
    }

    fun onLeftTopClick(view: View) {
        hitActions.doAction(Action(Position.LEFT_TOP.value))
    }
    fun onLeftBottomClick(view: View) {
        hitActions.doAction(Action(Position.LEFT_BOTTOM.value))

    }
    fun onRightBottomClick(view: View) {
        hitActions.doAction(Action(Position.RIGHT_BOTTOM.value))

    }
    fun onRightTopClick(view: View) {
        hitActions.doAction(Action(Position.RIGHT_TOP.value))

    }
}