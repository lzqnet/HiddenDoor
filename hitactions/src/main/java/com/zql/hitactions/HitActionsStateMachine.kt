package com.zql.hitactions

import android.os.Looper
import android.os.Message
import android.util.Log
import com.zql.hitactions.StateMachine.State
import com.zql.hitactions.StateMachine.StateMachine
import java.lang.StringBuilder

class HitActionsStateMachine(looper: Looper?) : StateMachine(TAG, looper) {
    companion object {
        private val TAG: String = HitActionsConstants.MODULE_TAG
        private val EVENT_STATE_DEFAULT = 0
        private val EVENT_STATE_IDLE = 1
        private val EVENT_EXECUTE_ACTION = 3
        private val EVENT_OVERTIME_MSG = 4

    }

    private var mOverTime: Long = HitActionsConstants.OVERTIME_MSEC
    private val mDefaultState = DefaultState()
    private val mIdleState = IdleState()
    private val mDetectingState = DetectingState()
    private val mStartupState = StartupState()
    private lateinit var actionArray: ArrayList<Action>
    private lateinit var actionListener: IActionListener

    init {
        addState(mDefaultState)
        addState(mStartupState, mDefaultState)
        addState(mIdleState, mDefaultState)
        addState(mDetectingState, mDefaultState)
        setInitialState(mStartupState)
    }

    fun setOverTimeMsec(value: Long) {
        mOverTime = value
    }

    fun setActionListener(listener: IActionListener) {
        actionListener = listener
        if (::actionArray.isInitialized && ::actionListener.isInitialized) {
            sendMessage(EVENT_STATE_IDLE)
        }
    }

    fun setActionArray(actions: ArrayList<Action>) {
        actionArray = actions
        if (::actionArray.isInitialized && ::actionListener.isInitialized) {
            sendMessage(EVENT_STATE_IDLE)
        }
    }

    fun doAction(action: Action) {
        sendMessage(EVENT_EXECUTE_ACTION, action)
    }

    private inner class StartupState : BaseState() {

        override fun enter() {
            Log.d(TAG, " StartupState enter: ")
            super.enter()

        }

        override fun exit() {
            Log.d(TAG, " StartupState exit: ")
            super.exit()
        }

        override fun processMessage(msg: Message): Boolean {
            Log.d(
                TAG, " StartupState processMessage: msg=" + dumpMessage(msg)
            )
            when (msg.what) {
                EVENT_STATE_IDLE -> {
                    if (::actionArray.isInitialized && ::actionListener.isInitialized) {
                        transitionTo(mIdleState)
                    } else {
                        deferMessage(msg)
                    }
                    return true
                }

                else -> {
                    deferMessage(msg)
                    return true
                }
            }
        }
    }

    private inner class DefaultState : BaseState() {
        override fun enter() {
            HitActionLog.d(TAG, " DefaultState enter: ")
            super.enter()
        }

        override fun exit() {
            HitActionLog.d(
                TAG, " DefaultState exit: "
            )
            super.exit()
        }

        override fun processMessage(msg: Message): Boolean {
            HitActionLog.d(
                TAG,
                " DefaultState processMessage: unhandle msg=" + dumpMessage(msg)
            )
            when (msg.what) {

                else -> {
                    HitActionLog.d(
                        TAG,
                        " DefaultState processMessage: unhandle msg=" + dumpMessage(msg)
                    )
                    return false //super.processMessage(msg);
                }
            }

        }
    }

    private inner class IdleState : BaseState() {
        override fun enter() {
            HitActionLog.d(TAG, " IdleState enter: ")
            super.enter()
        }

        override fun exit() {
            HitActionLog.d(TAG, " IdleState exit: ")
            super.exit()
        }

        override fun processMessage(msg: Message): Boolean {
            HitActionLog.d(TAG, " IdleState processMessage: msg=" + dumpMessage(msg))
            return when (msg.what) {
                EVENT_STATE_IDLE -> true
                EVENT_EXECUTE_ACTION -> {
                    transitionTo(mDetectingState)
                    deferMessage(msg)
                    return true
                }
                EVENT_OVERTIME_MSG -> {
                    return true
                }

                else -> {

                    return true

                }
            }
        }
    }


    private inner class DetectingState : BaseState() {
        var currentIndex: Int = -1
        val userActionList: ArrayList<Action> = ArrayList()

        override fun enter() {
            HitActionLog.d(TAG, " DetectingState enter: ")
            super.enter()
            currentIndex = -1
            userActionList.clear()
        }

        override fun exit() {
            HitActionLog.d(
                TAG, " DetectingState exit: "
            )
            super.exit()
        }

        override fun processMessage(msg: Message): Boolean {
            HitActionLog.d(
                TAG,
                " DetectingState processMessage:  msg=" + dumpMessage(msg)
            )
            when (msg.what) {
                EVENT_STATE_IDLE -> {
                    transitionTo(mIdleState)
                    return true
                }

                EVENT_EXECUTE_ACTION -> {
                    try {
                        currentIndex++
                        HitActionLog.d(TAG, "DETECHING STATE currentIndex=$currentIndex")

                        val action = (msg.obj) as Action
                        doAction(action)
                    } catch (e: Throwable) {
                        HitActionLog.e(TAG, "DETECHING STATE", e)
                    } finally {
                        return true
                    }


                }
                EVENT_OVERTIME_MSG -> {
                    HitActionLog.d(
                        TAG,
                        "DetectingState processMessage action overtime.match action is ${
                            dumpArrayList(
                                userActionList
                            )
                        }"
                    )
                    transitionTo(mIdleState)
                    actionListener.onActionStateChange(false)
                    return true
                }
                else -> {
                    HitActionLog.d(
                        TAG,
                        " DetectingState processMessage: unhandle msg=" + dumpMessage(msg)
                    )
                    return false //super.processMessage(msg);
                }
            }

        }

        private fun doAction(action: Action) {
            HitActionLog.w(TAG, "doAction enter")

            if (handler.hasMessages(EVENT_OVERTIME_MSG)) {
                HitActionLog.d(TAG, "doAction remove EVENT_OVERTIME_MSG")
                handler.removeMessages(EVENT_OVERTIME_MSG)
            }
            userActionList.add(action)
            if (!::actionArray.isInitialized || !::actionListener.isInitialized) {
                HitActionLog.w(TAG, "doAction actionArray or actionListener is not init")
                transitionTo(mIdleState)
                return

            }
            if (currentIndex >= actionArray.size || currentIndex < 0) {
                HitActionLog.w(TAG, "doAction index is invalid")
                transitionTo(mIdleState)
                actionListener.onActionStateChange(false)
                return
            }

            val currentAction = actionArray.get(currentIndex)
            if (currentAction.actionId == action.actionId) {
                if (currentIndex != actionArray.size - 1) {
                    HitActionLog.d(TAG, "doAction action cuntinue")
                    HitActionLog.d(TAG, "doAction add  EVENT_OVERTIME_MSG")

                    sendMessageDelayed(EVENT_OVERTIME_MSG, mOverTime)
                    return
                }
                if (currentIndex == actionArray.size - 1) {
                    HitActionLog.d(TAG, "doAction action end onActionStateChange is true.")
                    actionListener.onActionStateChange(true)
                    transitionTo(mIdleState)
                }
            } else {
                HitActionLog.d(
                    TAG,
                    "doAction action end ,onActionStateChange is false.match action is ${
                        dumpArrayList(
                            userActionList
                        )
                    }"
                )
                actionListener.onActionStateChange(false)
                transitionTo(mIdleState)

            }


        }
    }

    private open class BaseState : State() {
        override fun enter() {}
        override fun exit() {}
        override fun processMessage(msg: Message): Boolean {
            when (msg.what) {

                else
                -> {
                    return false
                }


            }

        }
    }

    private fun dumpArrayList(arrayList: ArrayList<Action>): String {
        return arrayList.joinToString(
            prefix = "[",
            separator = ",",
            postfix = "]",
        )
    }

    private fun dumpMessage(msg: Message): String {
        val builder = StringBuilder()
        builder.append("msg={")
        when (msg.what) {
            EVENT_STATE_IDLE -> builder.append("what= EVENT_STATE_IDLE")
            EVENT_OVERTIME_MSG -> builder.append("what=EVENT_OVERTIME_MSG")
            EVENT_EXECUTE_ACTION -> builder.append("what=EVENT_EXECUTE_ACTION")

            else -> builder.append(msg.what)
        }
        builder.append("; arg1=" + msg.arg1).append("; arg2=" + msg.arg2)
        if (msg.obj != null) {
            builder.append("; obj=" + msg.obj.toString() + "}")
        }
        return builder.toString()
    }

}