package com.zql.hitactions

import android.os.Looper
import android.os.Message
import com.zql.hitactions.HitActionsConstants.Companion.DEFAULT_ACTIONNAME
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

        private val EVENT_SETACTIONARRAY = 5
        private val EVENT_SETACTIONLISTENER = 6
        private val EVENT_ADD_ACTION_ARRAY = 7
        private val EVENT_REMOVE_ACTION_ARRAY = 8

    }

    private var mOverTime: Long = HitActionsConstants.OVERTIME_MSEC
    private val mDefaultState = DefaultState()
    private val mIdleState = IdleState()
    private val mDetectingState = DetectingState()
    private val mStartupState = StartupState()
    private lateinit var actionArray: HashMap<String, ArrayList<Action>>
    private lateinit var actionListener: HashMap<String, IActionListener>

    init {
        addState(mDefaultState)
        addState(mStartupState, mDefaultState)
        addState(mIdleState, mDefaultState)
        addState(mDetectingState, mDefaultState)
        setInitialState(mStartupState)
    }

    fun setOverTimeMsec(value: Long) {
        HitActionLog.d(TAG, "setOverTimeMsec")
        mOverTime = value
    }

    @Deprecated("using addActionArray replace")
    fun setActionArray(actions: ArrayList<Action>) {
        HitActionLog.d(TAG, "setActionArray")
        sendMessage(EVENT_SETACTIONARRAY, actions)

    }

    @Deprecated("using addActionArray replace")
    fun setActionListener(listener: IActionListener) {
        HitActionLog.d(TAG, "setActionListener")
        sendMessage(EVENT_SETACTIONLISTENER, listener)
    }

    fun addActionArray(
        actionName: String,
        actionList: ArrayList<Action>,
        listener: IActionListener,
        callback: IAddActionResult?
    ) {
        HitActionLog.d(TAG, "addActionArray")
        sendMessage(
            EVENT_ADD_ACTION_ARRAY,
            AddActionParam(actionName, actionList, listener, callback)
        )
    }

    fun removeActionArray(actionName: String = DEFAULT_ACTIONNAME) {
        HitActionLog.d(TAG, "removeActionArray")
        sendMessage(EVENT_REMOVE_ACTION_ARRAY, actionName)
    }


    fun doAction(action: Action) {
        sendMessage(EVENT_EXECUTE_ACTION, action)
    }

    private inner class StartupState : BaseState() {

        override fun enter() {
            HitActionLog.d(TAG, "StartupState enter: ")
            super.enter()

        }

        override fun exit() {
            HitActionLog.d(TAG, "StartupState exit: ")
            super.exit()
        }

        override fun processMessage(msg: Message): Boolean {
            HitActionLog.d(
                TAG, "StartupState processMessage: msg=" + dumpMessage(msg)
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
                    return false
                }
            }
        }
    }

    private inner class DefaultState : BaseState() {
        override fun enter() {
            HitActionLog.d(TAG, "DefaultState enter: ")
            super.enter()
        }

        override fun exit() {
            HitActionLog.d(
                TAG, "DefaultState exit: "
            )
            super.exit()
        }

        override fun processMessage(msg: Message): Boolean {
            HitActionLog.d(TAG, "DefaultState processMessage: msg=" + dumpMessage(msg))
            try {
                when (msg.what) {
                    EVENT_SETACTIONARRAY -> {
                        val actions = msg.obj as ArrayList<Action>
                        if (!::actionArray.isInitialized) {
                            actionArray = HashMap()
                        }
                        HitActionLog.d(
                            TAG,
                            "DefaultState processMessage EVENT_SETACTIONARRAY: put DEFAULT_ACTIONNAME"
                        )
                        actionArray.put(DEFAULT_ACTIONNAME, actions)
                        if (::actionArray.isInitialized && ::actionListener.isInitialized) {
                            HitActionLog.d(
                                TAG,
                                "DefaultState processMessage EVENT_SETACTIONARRAY: sendMessage(EVENT_STATE_IDLE)"
                            )
                            sendMessage(EVENT_STATE_IDLE)
                        }
                        return true

                    }
                    EVENT_SETACTIONLISTENER -> {
                        val listener = msg.obj as IActionListener
                        if (!::actionListener.isInitialized) {
                            actionListener = HashMap()
                        }
                        HitActionLog.d(
                            TAG,
                            "DefaultState processMessage EVENT_SETACTIONLISTENER: put DEFAULT_ACTIONNAME"
                        )
                        actionListener.put(DEFAULT_ACTIONNAME, listener)
                        if (::actionArray.isInitialized && ::actionListener.isInitialized) {
                            HitActionLog.d(
                                TAG,
                                "DefaultState processMessage EVENT_SETACTIONLISTENER: sendMessage(EVENT_STATE_IDLE)"
                            )
                            sendMessage(EVENT_STATE_IDLE)
                        }
                        return true
                    }
                    EVENT_ADD_ACTION_ARRAY -> {
                        val param = msg.obj as AddActionParam
                        if (!::actionArray.isInitialized) {
                            actionArray = HashMap()
                        }
                        HitActionLog.d(
                            TAG,
                            " DefaultState processMessage EVENT_ADD_ACTION_ARRAY  begin"
                        )
                        if (!actionArray.keys.contains(param.actionName)) {
                            HitActionLog.d(
                                TAG,
                                " DefaultState processMessage EVENT_ADD_ACTION_ARRAY  key is unuse"
                            )
                            actionArray.forEach {
                                val itemActionsArray = it.value
                                val itemActionName=it.key
                                var child: ArrayList<Action>
                                val parent = if (itemActionsArray.size >= param.actionList.size) {
                                    child = param.actionList
                                    itemActionsArray
                                } else {
                                    child = itemActionsArray
                                    itemActionsArray
                                }
                                HitActionLog.d(
                                    TAG,
                                    "DefaultState processMessage EVENT_ADD_ACTION_ARRAY   check  parent=${
                                        dumpArrayList(
                                            parent
                                        )
                                    };child=${
                                        dumpArrayList(child)
                                    }"
                                )
                                for (index in 0 until child.size) {
                                    if (child.get(index).actionId != parent.get(index).actionId) {
                                        break
                                    } else if (index == child.size - 1) {
                                        HitActionLog.w(
                                            TAG,
                                            " DefaultState processMessage EVENT_ADD_ACTION_ARRAY   actionname ${param.actionName} is invalid!!"
                                        )
                                        param.callback?.onAddStatus(false)
                                        return true
                                    }
                                }

                            }
                        }

                        HitActionLog.d(
                            TAG,
                            "DefaultState processMessage EVENT_ADD_ACTION_ARRAY: put ${param.actionName}"
                        )
                        actionArray.put(param.actionName, param.actionList)
                        param.callback?.onAddStatus(true)
                        if (!::actionListener.isInitialized) {
                            actionListener = HashMap()
                        }

                        actionListener.put(param.actionName, param.listener)
                        if (::actionArray.isInitialized && ::actionListener.isInitialized) {
                            HitActionLog.d(
                                TAG,
                                "DefaultState processMessage EVENT_ADD_ACTION_ARRAY: sendMessage(EVENT_STATE_IDLE)"
                            )
                            sendMessage(EVENT_STATE_IDLE)
                        }
                        return true

                    }
                    EVENT_REMOVE_ACTION_ARRAY -> {
                        val actionName = msg.obj as String
                        if (!::actionArray.isInitialized) {
                            actionArray = HashMap()
                        }
                        HitActionLog.d(
                            TAG,
                            "DefaultState processMessage EVENT_REMOVE_ACTION_ARRAY: remove $actionName"
                        )
                        actionArray.remove(actionName)

                        if (!::actionListener.isInitialized) {
                            actionListener = HashMap()
                        }
                        actionListener.remove(actionName)

                        if (::actionArray.isInitialized && ::actionListener.isInitialized) {
                            HitActionLog.d(
                                TAG,
                                "DefaultState processMessage EVENT_REMOVE_ACTION_ARRAY: sendMessage(EVENT_STATE_IDLE)"
                            )
                            sendMessage(EVENT_STATE_IDLE)
                        }
                        return true

                    }
                    else -> {
                        HitActionLog.d(
                            TAG,
                            " DefaultState processMessage: unhandle msg=" + dumpMessage(msg)
                        )
                        return true
                    }
                }
            } catch (e: Exception) {
                HitActionLog.e(
                    TAG,
                    " DefaultState Exception", e
                )
            }

            return true
        }
    }

    private data class AddActionParam(
        val actionName: String,
        val actionList: ArrayList<Action>,
        val listener: IActionListener,
        val callback: IAddActionResult?
    ) {

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

                    return false

                }
            }
        }
    }


    private inner class DetectingState : BaseState() {
        val userActionList: ArrayList<Action> = ArrayList()
        val result: HashMap<String, ActionState> = HashMap()

        override fun enter() {
            HitActionLog.d(TAG, " DetectingState enter: ")
            super.enter()
            userActionList.clear()
            result.clear()
        }

        override fun exit() {
            HitActionLog.d(
                TAG, " DetectingState exit: "
            )
            super.exit()
            userActionList.clear()
            result.clear()

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
                        val action = (msg.obj) as Action
                        doAction(action)
                    } catch (e: Throwable) {
                        HitActionLog.e(TAG, "DETECHING STATE,EVENT_EXECUTE_ACTION", e)
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
                    try {
                        actionListener.forEach {
                            val callback = it.value
                            callback.onActionStateChange(false)
                        }
                    } catch (e: Exception) {
                        HitActionLog.e(TAG, "DETECHING STATE,EVENT_OVERTIME_MSG", e)

                    }

                    return true
                }
                else -> {
                    HitActionLog.d(
                        TAG,
                        " DetectingState processMessage: unhandle msg=" + dumpMessage(msg)
                    )
                    return false
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

            actionArray.forEach {
                val actionName: String = it.key
                val actionList: ArrayList<Action> = it.value
                val actionState = matchActions(actionName, actionList, userActionList)
                result.put(actionName, actionState)

            }
// if any actionname has success.return
            if (result.values.contains(ActionState.SUCCESS)) {
                result.forEach {
                    val actionName: String = it.key
                    val actionState = it.value
                    val callback = actionListener.get(actionName)
                    if (actionState == ActionState.SUCCESS) {
                        callback?.onActionStateChange(true)
                    }
                }
                transitionTo(mIdleState)
                return

            }
            if (result.values.contains(ActionState.CONTINUE)) {
                sendMessageDelayed(EVENT_OVERTIME_MSG, mOverTime)
                return
            }
            result.forEach {
                val actionName: String = it.key
                val actionState = it.value
                val callback = actionListener.get(actionName)
                if (actionState == ActionState.ERROR) {
                    callback?.onActionStateChange(false)
                }
                transitionTo(mIdleState)
            }


        }

        private fun matchActions(
            actionName: String,
            actionList: ArrayList<Action>,
            userActionList: ArrayList<Action>
        ): ActionState {
            HitActionLog.d(
                TAG,
                "matchActions actionName=$actionName;actionList=${dumpArrayList(actionList)};userAction=${
                    dumpArrayList(userActionList)
                }"
            )

            val userActionsLen = userActionList.size
            val actionListLen = actionList.size

            if (userActionsLen > actionListLen || userActionsLen < 0) {
                HitActionLog.w(TAG, "matchActions actionName=$actionName userActionsLen is invalid")
                return ActionState.ERROR
            } else {
                for (index in 0 until userActionsLen) {
                    val userAction = userActionList.get(index)
                    val presetAction = actionList.get(index)
                    if (userAction.actionId == presetAction.actionId) {
                        if (index < userActionsLen - 1) {
                            continue
                        } else {
                            if (userActionsLen != actionListLen) {
                                HitActionLog.d(
                                    TAG,
                                    "matchActions actionName=$actionName action cuntinue"
                                )
                                HitActionLog.d(TAG, "matchActions add  EVENT_OVERTIME_MSG")
                                return ActionState.CONTINUE
                            } else {
                                HitActionLog.d(
                                    TAG,
                                    "matchActions actionName=$actionName .action end onActionStateChange is true."
                                )
                                return ActionState.SUCCESS
                            }
                        }
                    } else {
                        HitActionLog.d(
                            TAG,
                            "matchActions action match fail ,actionName=$actionName . onActionStateChange is false.match action is ${
                                dumpArrayList(
                                    userActionList
                                )
                            }"
                        )
                        return ActionState.ERROR

                    }
                }


            }
            HitActionLog.d(
                TAG,
                "matchActions action end ,actionName=$actionName . unknow error.match action is ${
                    dumpArrayList(
                        userActionList
                    )
                }"
            )
            return ActionState.ERROR
        }


    }

    enum class ActionState {
        ERROR, CONTINUE, SUCCESS
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

            EVENT_SETACTIONARRAY -> builder.append("what=EVENT_SETACTIONARRAY")
            EVENT_SETACTIONLISTENER -> builder.append("what=EVENT_SETACTIONLISTENER")
            EVENT_ADD_ACTION_ARRAY -> builder.append("what=EVENT_ADD_ACTION_ARRAY")
            EVENT_REMOVE_ACTION_ARRAY -> builder.append("what=EVENT_REMOVE_ACTION_ARRAY")
            else -> builder.append(msg.what)
        }
        builder.append("; arg1=" + msg.arg1).append("; arg2=" + msg.arg2)
        if (msg.obj != null) {
            builder.append("; obj=" + msg.obj.toString() + "}")
        }
        return builder.toString()
    }

}

