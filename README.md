背景
一般App都会有一个暗码入口（暗门），暗码点击正确后，会打开调试界面。但是目前的暗码都是在代码里写死，如果暗码被用户试出来，会存在一定的安全隐患。需要有一套可以线上配置的暗码工具

使用

1. 引入基础库：
  1. Add it in your root build.gradle at the end of repositories:
        allprojects {
                repositories {
                        ...
                        maven { url 'https://jitpack.io' }
                }
        }


  2.      if you using the newest gradle,  you should modify setting.gradle

    dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url 'https://jitpack.io' }
        google()
        mavenCentral()
    }
}
  3. Add the dependency
        dependencies {
                implementation 'com.github.lzqnet:HiddenDoor:1.5.0'
        }
2. 设定暗码可点击点的id
  1. 如示例中屏幕四个角分别定为1，2，3，4
3. 设置暗码，即第二步中设置的id的点击顺序
  1. 如示例中的var numberList:List<Int> = listOf(1,2,1,2,3,4,4)（一闪一闪亮晶晶）
4. 设置暗码成功或失败的回调
5. 给每个点击点设置单击发送id事件
private fun initHiddenDoor() {
    hitActions.init(Looper.getMainLooper())
    val list = ArrayList<Action>()
    //获取暗码
    HidenDoorNumber.numberList.forEach {
        list.add(Action(it))
    }
    //设置暗码
    hitActions.setActionSequence(list)
    //设置回调
    hitActions.setActionListener(object : IActionListener {
        override fun onActionStateChange(result: Boolean) {
            if (result) {
                openDebuger()
                Toast.makeText(applicationContext, 打开调试页面 ,Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, 匹配失败 ,Toast.LENGTH_SHORT).show()

            }
        }

    })
    //暗码组件打Log用
    HitActionLog.setLogger(HitLogger())

}

private fun openDebuger() {
    val intent=Intent()
    intent.setClassName(packageName, com.bytedance.hiddendoor.Debugger )
    startActivity(intent)
}
//暗码view的点击事件
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
  只要动态的去设置hitActions.setActionSequence即可实现暗码变更

<img src="https://github.com/lzqnet/HiddenDoor/blob/master/image/demo.gif" width="50%"  height="50%"  />
