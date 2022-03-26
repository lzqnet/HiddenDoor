# 背景 

一般App都会有一个暗码入口（暗门），暗码点击正确后，会打开调试界面。但是目前的暗码都是在代码里写死，如果暗码被用户试出来，会存在一定的安全隐患。需要有一套可以线上配置的暗码工具 

 

# 使用 

- 本组件用于处理如下场景：

- -  某个界面有多个透明的view,按照一定规则点击这些透明view.触发暗门入口。

**步骤**

1. 引入基础库：
   1. Add it in your root build.gradle at the end of repositories:

```Groovy
        allprojects {
                repositories {
                        ...
                        maven { url 'https://jitpack.io' }
                }
        }


```

-

- - ​    if you using the newest gradle,  you should modify setting.gradle
    1. ```Groovy
           dependencyResolutionManagement {
           repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
           repositories {
               maven { url 'https://jitpack.io' }
               google()
               mavenCentral()
           }
       }
       ```
  - Add the dependency
  - ```Groovy
    implementation 'com.github.lzqnet:HiddenDoor:2.1.0'
    ```

1. 为每个点击处编号（每个可点击的view进行编号）
   1. 给暗码每个可点击的地方编号，如示例中屏幕四个角分别编号为1，2，3，4
   2. ```Kotlin
      enum class Position(val value: Int) {
          LEFT_TOP(1),LEFT_BOTTOM(2),RIGHT_BOTTOM(3),RIGHT_TOP(4)

      }
      ```

   3. 为每个点击处增加单击事件，发送对应编号。以下四个方法分别是屏幕四个角对应的点击事件

```Kotlin
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
```

1. 预设暗码序列，即第二步中设置的id的点击顺序 。也就是要以什么顺序点击第二步设置的view，才能打开暗门。同时设置暗码输入成功或失败回调
   1. ```Kotlin
       hitActions.addActionArray(
       //暗码为按顺序点击编号为1、4的view
              arrayListOf<Action>(Action(1), Action(4)),
              object : IActionListener {
                  override fun onActionStateChange(result: Boolean) {
                      if (result) {
                      //暗码输入成功，打开debug页面
                          openDebuger()
                          Toast.makeText(applicationContext,  打开调试页面 add , Toast.LENGTH_SHORT).show()
                      } else {
                          Toast.makeText(applicationContext,  匹配失败 add , Toast.LENGTH_SHORT).show()

                      }
                  }

              },
              //这个回调用于接收此次添加暗码序列是否成功
              callback = object : IAddActionResult {
                  override fun onAddStatus(isAdded: Boolean) {
                      Log.d( hitactions ,  MainActivity.onAddStatus: add isAdded=$isAdded  );
                  }

              })
      }
      ```

各接口说明



```Kotlin
//所有对外提供功能的api都在此类里
class HitActions {
/*初始化，使用前应先调用该接口初始化。
*参数：looper 该组件运行在哪个线程上。一般传入Looper.getMainLooper() 即可。
*若不希望在主线程运行，可以传入子线程的looper
*/
    fun init(looper: Looper)
//已废弃，早期版本添加的接口
    fun setActionSequence(actions: ArrayList<Action>)
//已废弃，早期版本添加的接口
    fun setActionListener(listener: IActionListener)
/*
单击暗门view时，调用该方法，将自己的编号传进来。
比如：
fun onLeftTopClick(view: View) {
    hitActions.doAction(Action(Position.LEFT_TOP.value))
}
Position.LEFT_TOP.value 为使用步骤第二步介绍的预设编号
*/
    fun doAction(action: Action)

//设置暗门每个点击事件间隔时间
    fun setOverTimeMsec(value: Long)

/*
添加暗门序列，可添加多个。命中其中一个后，重新计算。
参数说明
actionList 暗门序列。即打开暗门时，每个可单击view的单击顺序
listener 用户点击成功或者失败的回调
actionName 标识暗门序列。组件支持多次调用addActionArray 传入多个暗门序列。actionName用
于标识每个暗门序列。执行时，只要命中传入的某个序列，即会回调对应的listener
callback 用来判断添加暗门序列是否成功的回调。
比如我先调用hitActions.addActionArray(
    arrayListOf<Action>(Action(1), Action(4)，Action(4)，Action(4)，Action(4))
    ...）.然后再调用hitActions.addActionArray(
    arrayListOf<Action>(Action(1), Action(4)，...）
    此时因为第二次调用传入的是第一次的子集，会与第一次传入的暗门序列冲突。第二次传入的暗门序列
    会添加失败
*/
    fun addActionArray(
        actionList: ArrayList<Action>,
        listener: IActionListener,
        actionName: String= DEFAULT_ACTIONNAME,
        callback:IAddActionResult?=null
    )

//移除某个暗门序列
    fun removeActionArray(actionName: String=DEFAULT_ACTIONNAME)
```

<img src= https://github.com/lzqnet/HiddenDoor/blob/master/image/demo.gif  width= 50%   height= 50%   />