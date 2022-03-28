package com.zql.hitactions

import android.os.Looper
import com.zql.hitactions.HitActionsConstants.Companion.DEFAULT_ACTIONNAME

class HitActions {
    private lateinit var stateMachine: HitActionsStateMachine
    fun init(looper: Looper) {
        if (!this::stateMachine.isInitialized) {
            HitActionLog.d(
                HitActionsConstants.MODULE_TAG,
                " init "
            )
            stateMachine = HitActionsStateMachine(looper)
            stateMachine.start()
        }
    }

    @Deprecated("using addActionArray replace")
    fun setActionSequence(actions: ArrayList<Action>) {
        if (!this::stateMachine.isInitialized) {
            HitActionLog.w(
                HitActionsConstants.MODULE_TAG,
                " setActionSequence  call init fun fist!!"
            )
            return
        }
        stateMachine.setActionArray(actions)
    }

    @Deprecated("using addActionArray replace")
    fun setActionListener(listener: IActionListener) {
        if (!this::stateMachine.isInitialized) {
            HitActionLog.w(
                HitActionsConstants.MODULE_TAG,
                " setActionSequence  call init fun fist!!"
            )
            return
        }
        stateMachine.setActionListener(listener)
    }

    fun doAction(action: Action) {
        if (!this::stateMachine.isInitialized) {
            HitActionLog.w(
                HitActionsConstants.MODULE_TAG,
                " setActionSequence  call init fun fist!!"
            )
            return
        }
        stateMachine.doAction(action)
    }

    fun setOverTimeMsec(value: Long) {
        if (!this::stateMachine.isInitialized) {
            HitActionLog.w(
                HitActionsConstants.MODULE_TAG,
                " setActionSequence  call init fun fist!!"
            )
            return
        }
        stateMachine.setOverTimeMsec(value)
    }

    fun addActionArray(
        actionList: ArrayList<Action>,
        listener: IActionListener,
        actionName: String = DEFAULT_ACTIONNAME,
        callback: IAddActionResult? = null
    ) {
        if (!this::stateMachine.isInitialized) {
            HitActionLog.w(
                HitActionsConstants.MODULE_TAG,
                " setActionSequence  call init fun fist!!"
            )
            return
        }
        stateMachine.addActionArray(actionName, actionList, listener, callback)
    }

    fun updateActionArray(
        actionList: ArrayList<Action>,
        actionName: String = DEFAULT_ACTIONNAME,
        callback: IAddActionResult? = null
    ) {
        if (!this::stateMachine.isInitialized) {
            HitActionLog.w(
                HitActionsConstants.MODULE_TAG,
                " updateActionArray  call init fun fist!!"
            )
            return
        }
        stateMachine.updateActionArray(actionList, actionName, callback)
    }

    fun updateActionListener(
        actionName: String = DEFAULT_ACTIONNAME,
        listener: IActionListener
    ) {
        if (!this::stateMachine.isInitialized) {
            HitActionLog.w(
                HitActionsConstants.MODULE_TAG,
                " updateActionListener  call init fun fist!!"
            )
            return
        }
        stateMachine.updateActionListener(actionName, listener)
    }

    fun removeActionArray(actionName: String = DEFAULT_ACTIONNAME) {
        if (!this::stateMachine.isInitialized) {
            HitActionLog.w(
                HitActionsConstants.MODULE_TAG,
                " setActionSequence  call init fun fist!!"
            )
            return
        }
        stateMachine.removeActionArray(actionName)
    }


}