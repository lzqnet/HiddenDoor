package com.zql.hitactions

import android.os.Looper

class HitActions {
    private lateinit var stateMachine: HitActionsStateMachine
    fun init(looper: Looper){
        if (!this::stateMachine.isInitialized) {
            stateMachine = HitActionsStateMachine(looper)
            stateMachine.start()
        }
    }
    fun setActionSequence(actions:ArrayList<Action>){
        if (!this::stateMachine.isInitialized) {
            return
        }
        stateMachine.setActionArray(actions)
    }

    fun setActionListener(listener: IActionListener) {
        if (!this::stateMachine.isInitialized) {
            return
        }
        stateMachine.setActionListener(listener)
    }

    fun doAction(action:Action){
        if (!this::stateMachine.isInitialized) {
            return
        }
        stateMachine.doAction(action)
    }

    fun setOverTimeMsec(value: Long) {
        if (!this::stateMachine.isInitialized) {
            return
        }
        stateMachine.setOverTimeMsec(value)
    }

}