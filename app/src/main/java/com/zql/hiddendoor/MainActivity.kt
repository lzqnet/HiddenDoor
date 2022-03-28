package com.zql.hiddendoor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.zql.hitactions.*
import java.lang.Exception


class MainActivity : AppCompatActivity() {
    private val hitActions: HitActions = HitActions()
    lateinit var editText:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editText=findViewById(R.id.edit) as EditText
        initHiddenDoor()
    }

    private fun initHiddenDoor() {
        hitActions.init(Looper.getMainLooper())
        val list1 = ArrayList<Action>()
        HidenDoorNumber.numberList1.forEach {
            list1.add(Action(it))
        }
        hitActions.setActionSequence(list1)
        hitActions.setActionListener(object : IActionListener {
            override fun onActionStateChange(result: Boolean) {
                if (result) {
                    openDebuger()
                    Toast.makeText(applicationContext, "打开调试页面", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "匹配失败", Toast.LENGTH_SHORT).show()

                }
            }

        })
        val list2 = ArrayList<Action>()
        HidenDoorNumber.numberList2.forEach {
            list2.add(Action(it))
        }
        hitActions.addActionArray(list2, object : IActionListener {
            override fun onActionStateChange(result: Boolean) {
                if (result) {
                    openDebuger()
                    Toast.makeText(applicationContext, "打开调试页面2", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "匹配失败2", Toast.LENGTH_SHORT).show()

                }
            }

        }, "2", object : IAddActionResult {
            override fun onAddStatus(isAdded: Boolean) {
                Log.d("hitactions", "MainActivity.onAddStatus: list2 isAdded=$isAdded ");
            }

        })


        val list3 = ArrayList<Action>()
        HidenDoorNumber.numberList3.forEach {
            list3.add(Action(it))
        }
        hitActions.addActionArray(list3, object : IActionListener {
            override fun onActionStateChange(result: Boolean) {
                if (result) {
                    openDebuger()
                    Toast.makeText(applicationContext, "打开调试页面3", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "匹配失败3", Toast.LENGTH_SHORT).show()

                }
            }

        }, "3")

        val list4 = ArrayList<Action>()
        HidenDoorNumber.numberList4.forEach {
            list4.add(Action(it))
        }
        hitActions.addActionArray(list4, object : IActionListener {
            override fun onActionStateChange(result: Boolean) {
                if (result) {
                    openDebuger()
                    Toast.makeText(applicationContext, "打开调试页面4", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "匹配失败4", Toast.LENGTH_SHORT).show()

                }
            }

        }, "4", callback = object : IAddActionResult {
            override fun onAddStatus(isAdded: Boolean) {
                Log.d("hitactions", "MainActivity.onAddStatus: list4 isAdded=$isAdded ");
            }

        })
//        hitActions.setActionListener(object : IActionListener {
//            override fun onActionStateChange(result: Boolean) {
//                if (result) {
//                    openDebuger()
//                    Toast.makeText(applicationContext,"打开调试页面",Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(applicationContext,"匹配失败",Toast.LENGTH_SHORT).show()
//
//                }
//            }
//
//        })
        HitActionLog.setLogger(HitLogger())

    }

    private fun openDebuger() {
        val intent = Intent()
        intent.setClassName(packageName, "com.zql.hiddendoor.Debugger")
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

    fun onADDClick(view: View) {
        hitActions.addActionArray(
            arrayListOf<Action>(Action(1), Action(4)),
            object : IActionListener {
                override fun onActionStateChange(result: Boolean) {
                    if (result) {
                        openDebuger()
                        Toast.makeText(applicationContext, "打开调试页面 add", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, "匹配失败 add", Toast.LENGTH_SHORT).show()

                    }
                }

            },
            callback = object : IAddActionResult {
                override fun onAddStatus(isAdded: Boolean) {
                    Log.d("hitactions", "MainActivity.onAddStatus: add isAdded=$isAdded ");
                }

            })
    }

    fun onRemoveClick(view: View) {
        hitActions.removeActionArray()
    }

    fun onUpdateArrayClick(view: View) {
        var string=editText.editableText
        var arrayString :List<Int>
        try {
          arrayString =string.toString().removeSurrounding("[","]").replace(" ","").split(",").map { it.toInt() }
            val list = ArrayList<Action>()
            arrayString.forEach {
                list.add(Action(it))
            }
            hitActions.updateActionArray(list,"4",object :IAddActionResult{
                override fun onAddStatus(isAdded: Boolean) {
                    Log.e("hitactions", "onUpdateArrayClick:onAddStatus isAdded=$isAdded");
                }

            })

        }catch (e:Exception){
            Log.e("hitactions", "onUpdateArrayClick:",e);

        }
    }
    fun onUpdateListenerClick(view: View) {

        try {

            hitActions.updateActionListener("4",object :IActionListener{
                override fun onActionStateChange(isMatch: Boolean) {
                    Log.e("hitactions", "onActionStateChange  updateActionListener:isMatch=$isMatch");

                }
            }
               )

        }catch (e:Exception){
            Log.e("hitactions", "onUpdateArrayClick:",e);

        }
    }
}