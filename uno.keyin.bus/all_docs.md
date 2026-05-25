# 小米Vela文档集合

本文档由多个HTML文件合并而成。


---
## 文件: index.html

# JS应用开发文档




---
## 文件: components\index.html

# # 组件

# [#](index.html#组件) 组件
 组件分为预定义组件和自定义组件。
 预定义组件是框架预先定义好、由框架实现渲染和逻辑的组件，例如开发页面时开发者必须用到的 text、div，这些组件是由平台 Native 底层渲染出来的。
 如果开发一个复杂的页面，开发者把所有的 UI 部分写在一个文件的``，那代码的可维护性将会很低，并且模块之间容易产生不必要的耦合关系，为了更好地组织逻辑与代码，可以把页面按照功能拆成多个模块，每个模块负责其中的一个功能部分，最后页面将这些模块引入管理起来，传递业务与配置数据完成代码分离，那么这就是自定义组件的意义。
 [自定义组件](../guide/framework/template/component.html)是一个开发者编写的组件，使用起来和 Native 组件一样，最终按照组件的``来渲染；同时开发起来又和页面一样，拥有 ViewModel 实现对数据、事件、方法的管理。
 这么来看，页面也是一种特殊的自定义组件，无需引入即可使用，同时服务于整个页面。
 本章节主要详细介绍预定义的 Native 组件的用法，包括其支持的样式、属性、事件。


---
## 文件: components\basic\a.html

# # a

# [#](a.html#a) a
 ## [#](a.html#概述) 概述
 超链接（默认不带下划线）
 ## [#](a.html#子组件) 子组件
 仅支持[``](span.html)
 ## [#](a.html#属性) 属性
 支持[通用属性](../general/properties.html)
 | 名称  | 类型  | 默认值  | 必填  | 描述  | href  | `string`  | -  | 否  | 支持的格式参见[页面路由](../../features/basic/router.html)中的 uri 参数。
额外的：
href 还可以通过“?param1=value1”的方式添加参数，参数可以在页面中通过`this.param1`的方式使用。使用`this.param1`变量时，需要在目标页面中在 `public`（应用外传参）或 `protected`（应用内传参）下定义 key 名相同的属性
示例：
`关于`  ## [#](a.html#样式) 样式
 支持[text样式](text.html)
 支持[通用样式](../general/style.html)
 ## [#](a.html#事件) 事件
 支持[通用事件](../general/events.html)
 ## [#](a.html#示例代码) 示例代码
 ```

    goHome
    
      使用span子组件

```


---
## 文件: components\basic\barcode.html

# # barcode2+

# [#](barcode.html#barcode) barcode[2+](../../guide/version/APILevel2.html)
 ## [#](barcode.html#概述) 概述
 条形码，将文本内容转换为条形码展示。
 ## [#](barcode.html#子组件) 子组件
 不支持
 ## [#](barcode.html#属性) 属性
 支持[通用属性](../general/properties.html)
 | 名称  | 类型  | 默认值  | 必填  | 描述  | value  | `string`  | -  | 是  | 条形码内容，码制为Code128码，长度小于等于20字节  ## [#](barcode.html#样式) 样式
 支持[通用样式](../general/style.html)
 | 名称  | 类型  | 默认值  | 必填  | 描述  | color  | ``  | #000000  | 否  | 条形码颜色  | background-color  | ``  | #ffffff  | 否  | 条形码背景颜色  注意
 
- 当设置transform的rotate属性时，该组件只能旋转为垂直或者水平状态；
 - 当设置transform的scale属性时，该组件只能支持整数倍缩放。

 ## [#](barcode.html#事件) 事件
 支持[通用事件](../general/events.html)
 ## [#](barcode.html#示例代码) 示例代码
 ```

```
![](../../../images/components/barcode.png)


---
## 文件: components\basic\chart.html

# # chart

# [#](chart.html#chart) chart
 ## [#](chart.html#概述) 概述
 图表组件，用于呈现线形图、柱状图界面。
 ## [#](chart.html#子组件) 子组件
 不支持
 ## [#](chart.html#属性) 属性
 支持[通用属性](../general/properties.html)
 | 名称  | 类型  | 默认值  | 必填  | 描述  | type  | ``  | line  | 否  | 设置图表类型（不支持动态修改），可选项有：bar（柱状图） \ line（线形图）  | options  | [ChartOptions](chart.html#chartoptions-%E8%AF%B4%E6%98%8E)  | -  | 是  | 图表参数设置，柱状图和线形图必须设置参数。可以设置x轴、y轴的最小值、最大值、刻度数、是否显示、线条宽度、是否平滑等。（不支持动态修改）  | datasets  | Array  | -  | 是  | 数据集合，柱状图和线形图必须设置，可以设置多条数据集及其背景色  ### [#](chart.html#chartoptions-说明) ChartOptions 说明
 | 名称  | 类型  | 默认值  | 必填  | 描述  | xAxis  | [ChartAxis](chart.html#chartaxis-%E8%AF%B4%E6%98%8E)  | line  | 是  | x轴参数设置。可以设置x轴最小值、最大值、刻度数以及是否显示  | yAxis  | [ChartAxis](chart.html#chartaxis-%E8%AF%B4%E6%98%8E)  | -  | 是  | y轴参数设置。可以设置y轴最小值、最大值、刻度数以及是否显示  | series  | [ChartSeries](chart.html#chartseries-%E8%AF%B4%E6%98%8E)  | -  | 否  | 数据序列参数设置。可以设置 1）线的样式，如线宽、是否平滑；2）设置线最前端位置白点的样式和大小（仅线形图支持）  ### [#](chart.html#chartdataset-说明) ChartDataset 说明
 | 名称  | 类型  | 默认值  | 必填  | 描述  | strokeColor  | ``  | #ff6384  | 否  | 线条颜色。（仅线形图支持）  | fillColor  | ``  | #ff6384  | 否  | 填充颜色。线形图表示填充的渐变颜色  | data  | Array  | -  | 是  | 设置绘制线或柱中的点集  | gradient  | ``  | false  | 否  | 设置是否显示填充渐变颜色。（仅线形图支持）  ### [#](chart.html#chartaxis-说明) ChartAxis 说明
 | 名称  | 类型  | 默认值  | 必填  | 描述  | min  | ``  | 0  | 否  | 轴的最小值。（不支持负数。仅线形图支持）  | max  | ``  | dataset 数据个数-1  | 否  | 轴的最大值。（不支持负数。仅线形图支持）  | axisTick  | ``  | 10  | 否  | 轴显示的刻度数量。（仅支持1~20，且具体显示的效果与如下计算值有关（图的宽度所占的像素/（max-min））。因轻量级智能穿戴为整型运行，在除不尽的情况下会有误差产生，具体的表现形式是x轴末尾可能会空出一段。在柱状图中，每组数据显示的柱子数量与刻度数量一致，且柱子显示在刻度处。）  | display  | ``  | false  | 否  | 是否显示轴  | color  | ``  | #c0c0c0  | 否  | 轴颜色  ### [#](chart.html#chartseries-说明) ChartSeries 说明
 | 名称  | 类型  | 默认值  | 必填  | 描述  | lineStyle  | [ChartLineStyle](chart.html#chartlinestyle-%E8%AF%B4%E6%98%8E)  | -  | 否  | 线样式设置，如线宽、是否平滑  | loop  | [ChartLoop](chart.html#chartloop-%E8%AF%B4%E6%98%8E)  | -  | 否  | 设置屏幕显示满时，是否需要重头开始绘制  ### [#](chart.html#chartlinestyle-说明) ChartLineStyle 说明
 | 名称  | 类型  | 默认值  | 必填  | 描述  | width  | ``  | 2px  | 否  | 线宽设置  ### [#](chart.html#chartloop-说明) ChartLoop 说明
 | 名称  | 类型  | 默认值  | 必填  | 描述  | margin  | ``  | 1px  | 否  | 擦除点的个数（最新绘制的点与最老的点之间的横向距离）。注意：轻量设备margin和topPoint/bottomPoint/headPoint同时使用时，有概率出现point正好位于擦除区域的情况，导致point不可见，因此不建议同时使用  ## [#](chart.html#样式) 样式
 支持[通用样式](../general/style.html)
 ## [#](chart.html#事件) 事件
 支持[通用事件](../general/events.html)
 ## [#](chart.html#示例代码) 示例代码
 ### [#](chart.html#线形图) 线形图
 ```

  export default {
    data: {
      lineData: [
        {
          strokeColor: '#f07826',
          data: [763, 550, 551, 554, 731, 654, 525, 696, 595, 628],
        },
        {
          strokeColor: '#cce5ff',
          fillColor: '#cce5ff', 
          data: [535, 776, 615, 444, 694, 785, 677, 609, 562, 410],
        },
        {
          strokeColor: '#ff88bb',
          data: [673, 500, 574, 483, 702, 583, 437, 506, 693, 657]
        },
      ],
      lineOpts: {
        xAxis: {
          min: 0,
          max: 10,
          display: true,
          axisTick: 10
        },
        yAxis: {
          min: 400,
          max: 900,
          display: true,
        }
      }
    }
  }

```
![](../../../images/components/line-chart.png)
 ### [#](chart.html#柱状图) 柱状图
 ```

  export default {
    data: {
      barData: [
        {
          fillColor: '#f07826',
          data: [763, 550, 551, 554, 731, 654, 525]
        },
        {
          fillColor: '#cce5ff',
          data: [535, 776, 615, 444, 694, 785, 677]
        }
      ],
      barOpts: {
        xAxis: {
          min: 0,
          max: 7,
          display: false,
          axisTick: 7
        },
        yAxis: {
          min: 0,
          max: 800,
          display: false,
        }
      }
    }
  }

```
![](../../../images/components/bar-chart.png)


---
## 文件: components\basic\image-animator.html

# # image-animator2+

# [#](image-animator.html#image-animator) image-animator[2+](../../guide/version/APILevel2.html)
 ## [#](image-animator.html#概述) 概述
 图片帧动画播放器。
 ## [#](image-animator.html#子组件) 子组件
 不支持
 ## [#](image-animator.html#属性) 属性
 支持[通用属性](../general/properties.html)
 | 名称  | 类型  | 默认值  | 必填  | 描述  | images  | `Array`  | -  | 是  | 设置图片帧信息集合。每一帧的帧信息包含图片路径、图片大小和图片位置信息  | iteration  | ``|``  | `infinite`  | 否  | 设置帧动画播放次数。number表示固定次数，infinite枚举表示无限次数播放  | reverse  | ``  | `false`  | 否  | 设置播放顺序。false表示从第1张图片播放到最后1张图片； true表示从最后1张图片播放到第1张图片  | fixedsize  | ``  | `true`  | 否  | 设置图片大小是否固定为组件大小。 true表示图片大小与组件大小一致，此时设置图片的width 、height 、top 和left属性是无效的。false表示每一张图片的 width 、height 、top和left属性都要单独设置  | duration  | ``  | -  | 否  | 设置单次播放时长。单位支持[s(秒)|ms(毫秒)]，默认单位为ms  | fillmode  | ``  | `forwards`  | 否  | 指定帧动画执行结束后的状态。可选项有：none：恢复初始状态。forwards：保持帧动画结束时的状态（在最后一个关键帧中定义）  ImageFrame说明
 | 名称  | 类型  | 默认值  | 必填  | 描述  | src  | ``  | -  | 是  | 图片路径  | width  | ``  | 0  | 否  | 图片宽度  | height  | ``  | 0  | 否  | 图片高度  | top  | ``  | 0  | 否  | 图片相对于组件左上角的纵向坐标  | left  | ``  | 0  | 否  | 图片相对于组件左上角的横向坐标  ## [#](image-animator.html#样式) 样式
 支持[通用样式](../general/style.html)
 ## [#](image-animator.html#方法) 方法
 支持[通用方法](../general/methods.html)
 | 名称  | 参数  | 描述  | start  | -  | 开始播放图片帧动画。再次调用，重新从第1帧开始播放  | pause  | -  | 暂停播放图片帧动画  | stop  | -  | 停止播放图片帧动画  | resume  | -  | 继续播放图片帧  | getState  | -  | 获取播放状态。- playing：播放中 - paused：已暂停 - stopped：已停止  ## [#](image-animator.html#示例代码) 示例代码
 ```

```
```
.container {
  flex-direction: column;
  justify-content: center;
  align-items: center;
  left: 0px;
  top: 0px;
  width: 454px;
  height: 454px;
  background-color: black;
}
.animator {
  width: 70px;
  height: 70px;
}
.btn-box {
  width: 264px;
  height: 120px;
  flex-wrap: wrap;
  justify-content: space-around;
  align-items: center;
}
.btn {
  border-radius: 8px;
  width: 120px;
  margin-top: 8px;
}

```
```
export default {
  data: {
    frames: [
      {
        src: "/common/asserts/001.png",
      },
      {
        src: "/common/asserts/002.png",
      },
      {
        src: "/common/asserts/003.png",
      },
      {
        src: "/common/asserts/004.png",
      },
      {
        src: "/common/asserts/005.png",
      }
    ],
  },
  handleStart() {
    this.$element('animator').start();
  },
  handlePause() {
    this.$element('animator').pause();
  },
  handleResume() {
    this.$element('animator').resume();
  },
  handleStop() {
    this.$element('animator').stop();
  },
};

```
![](../../../images/components/image_animator.gif)


---
## 文件: components\basic\image.html

# # image

# [#](image.html#image) image
 ## [#](image.html#概述) 概述
 渲染图片
 ## [#](image.html#子组件) 子组件
 不支持
 ## [#](image.html#属性) 属性
 支持[通用属性](../general/properties.html)
 | 名称  | 类型  | 默认值  | 必填  | 描述  | src  | ``  | -  | 否  | 图片的 uri，同时支持本地和云端路径，支持的图片格式包括png，jpg  | alt  | `` | 'blank'  | -  | 否  | 加载时显示的占位图；只支持本地图片资源  注意：alt 属性详情如下：
 
- 如果 Image 组件没有设置 alt 值，终端会加上默认的灰色占位图；

 - src 为本地图片地址时，不会有占位图；

 - src 为远程图片地址时，如果之前已经成功加载过图片，有本地缓存，则不会有占位图；

 - src 为远程图片地址时，且 Image 组件 的 alt 值传入字符串 "blank" 值，不会有占位图（可避免一些远程地址的小图标第一次加载时瞬间闪烁的现象）；

 - 设置 alt 为本地图片地址时，占位图缩放模式由原来的居中不缩放改为居中保持宽高比缩放，减少了占位图资源文件的分辨率与体积大小。

 注：缩放模式可以通过样式值`object-fit`配置，默认值为`cover`（居中保持宽高比缩放），详情查看[样式](image.html#%E6%A0%B7%E5%BC%8F)一节
 ## [#](image.html#样式) 样式
 支持[通用样式](../general/style.html)
 | 名称  | 类型  | 默认值  | 必填  | 描述  | object-fit  | contain | cover | none | scale-down  | cover  | 否  | 图片的缩放类型  注意：
 
1. object-fit参数列表如下：

 | 类型  | 描述  | contain  | 保持宽高比，缩小或者放大，使得图片完全显示在显示边界内，居中显示  | cover  | 保持宽高比，缩小或者放大，使得两边都大于或等于显示边界，居中显示  | none  | 居中，无缩放  | scale-down  | 保持宽高比，缩小或保持不变，取 `contain` 和 `none`中显示较小的一个，居中显示  ## [#](image.html#事件) 事件
 支持[通用事件](../general/events.html)
 | 名称  | 参数  | 描述  | complete  | {width: widthValue(px), height: heightValue(px)}  | 图片加载完成时触发  | error  | -  | 图片加载失败时触发  ## [#](image.html#示例代码) 示例代码
 ```

```
![](../../../images/components/image-example.png)


---
## 文件: components\basic\index.html

# # 基础组件

# [#](index.html#基础组件) 基础组件
 页面中的组件，例如文字、图片。
 
- [text](text.html)
 - [span](span.html)
 - [a](a.html)
 - [image](image.html)
 - [image-animator](image-animator.html)
 - [progress](progress.html)
 - [marquee](marquee.html)
 - [chart](chart.html)
 - [qrcode](qrcode.html)
 - [barcode](barcode.html)


---
## 文件: components\basic\marquee.html

# # marquee

# [#](marquee.html#marquee) marquee
 ## [#](marquee.html#概述) 概述
 跑马灯，用来插入一段滚动的文字，默认为单行。
 ## [#](marquee.html#子组件) 子组件
 不支持
 ## [#](marquee.html#属性) 属性
 支持[通用属性](../general/properties.html)
 | 名称  | 类型  | 默认值  | 必填  | 描述  | scrollamount  | ``  | 6  | 否  | 设置每次滚动时移动的长度，单位：px  | loop  | ``  | -1  | 否  | 设置 marquee 滚动的次数。如果未指定值，默认值为 −1，表示 marquee 将连续滚动  | direction  | ``  | left  | 否  | 文字滚动方向，支持 left，right  | text-offset  | ``  | -  | 否  | 设置跑马灯首尾相接时，上一段的尾和下一段的头之间的距离，属性值为大于 0 的整数，单位：px  ## [#](marquee.html#样式) 样式
 支持[通用样式](../general/style.html)
 | 名称  | 类型  | 默认值  | 必填  | 描述  | color  | ``  | rgba(0, 0, 0, 0.54)  | 否  | 文本颜色  | font-size  | ``  | 30px  | 否  | 文本尺寸  ## [#](marquee.html#事件) 事件
 支持[通用事件](../general/events.html)
 | 名称  | 参数  | 描述  | bounce  | -  | 当 marquee 滚动到结尾时触发  | finish  | -  | 当 marquee 完成设置的 loop 次数时触发，loop > 0 时有效  | start  | -  | 当 marquee 开始滚动时触发  ## [#](marquee.html#方法) 方法
 | 名称  | 参数  | 描述  | start  | -  | 开始滚动 marquee  | stop  | -  | 停止滚动 marquee  ## [#](marquee.html#示例代码) 示例代码
 ```

      scrollamount控制滚动速度，默认值为6（6像素/秒）

  export default {
    onReady() {
      this.$element('marquee').start()
    }
  }

```
![](../../../images/components/marquee.gif)


---
## 文件: components\basic\progress.html

# # progress

# [#](progress.html#progress) progress
 ## [#](progress.html#概述) 概述
 进度条
 ## [#](progress.html#子组件) 子组件
 不支持
 ## [#](progress.html#属性) 属性
 支持[通用属性](../general/properties.html)
 | 名称  | 类型  | 默认值  | 必填  | 描述  | percent  | ``  | 0  | 否  | -  | type  | horizontal | arc  | horizontal  | 否  | 进度条类型，不支持动态修改  ## [#](progress.html#样式) 样式
 支持[通用样式](../general/style.html)
 注：horizontal progress 底色为#f0f0f0；height 属性失效
 | 名称  | 类型  | 默认值  | 必填  | 描述  | color  | ``  | #33b4ff 或者 rgb(51, 180, 255)  | 否  | 进度条的颜色  | stroke-width  | ``  | 32px  | 否  | 进度条的宽度  | layer-color  | ``  | #f0f0f0 或者 rgb(240, 240, 240)  | 否  | 进度条的背景颜色  type=arc时生效：
 | 名称  | 类型  | 默认值  | 必填  | 描述  | start-angle  | ``  | 240  | 否  | 弧形进度条起始角度，以时钟0点为基线。范围为0到360（顺时针）  | total-angle  | ``  | 240  | 否  | 弧形进度条总长度，范围为-360到360，负数表示起点到终点为逆时针  | center-x  | ``  | 组件宽度的一半  | 否  | 弧形进度条中心位置，（坐标原点为组件左上角顶点）。该样式需要和 center-y \ radius 一起使用  | center-y  | ``  | 组件高度的一半  | 否  | 弧形进度条中心位置，（坐标原点为组件左上角顶点）。该样式需要和 center-x \ radius 一起使用  | radius  | ``  | 组件宽高较小值的一半  | 否  | 弧形进度条半径，该样式需要和 center-x \ center-y 一起使用  ## [#](progress.html#事件) 事件
 支持[通用事件](../general/events.html)
 ## [#](progress.html#示例代码) 示例代码
 ```

  .p1 {
    margin-bottom: 10px;
    stroke-width: 12px;
  }

  .p2 {
    margin-bottom: 10px;
    stroke-width: 12px;
    start-angle: 0;
    total-angle: 360deg;
  }

```
![](../../../images/components/progress.png)


---
## 文件: components\basic\qrcode.html

# # qrcode2+

# [#](qrcode.html#qrcode) qrcode[2+](../../guide/version/APILevel2.html)
 ## [#](qrcode.html#概述) 概述
 生成并显示二维码。
 ## [#](qrcode.html#子组件) 子组件
 不支持
 ## [#](qrcode.html#属性) 属性
 支持[通用属性](../general/properties.html)
 | 名称  | 类型  | 默认值  | 必填  | 描述  | value  | `string`  | -  | 是  | 用来生成二维码的内容  ## [#](qrcode.html#样式) 样式
 支持[通用样式](../general/style.html)
 | 名称  | 类型  | 默认值  | 必填  | 描述  | color  | ``  | #000000  | 否  | 二维码颜色  | background-color  | ``  | #ffffff  | 否  | 二维码背景颜色  ## [#](qrcode.html#事件) 事件
 支持[通用事件](../general/events.html)
 ## [#](qrcode.html#示例代码) 示例代码
 ```

```
![](../../../images/components/qrcode.png)


---
## 文件: components\basic\span.html

# # span

# [#](span.html#span) span
 ## [#](span.html#概述) 概述
 格式化的文本，只能作为[``](text.html)、[``](a.html)和``的子组件。
 ## [#](span.html#子组件) 子组件
 仅支持``
 ## [#](span.html#属性) 属性
 | 名称  | 类型  | 默认值  | 必填  | 描述  | id  | ``  | -  | 否  | 唯一标识  | style  | ``  | -  | 否  | 样式声明  | class  | ``  | -  | 否  | 引用样式表  | for  | ``  | -  | 否  | 根据数据列表，循环展开当前标签  | if  | ``  | -  | 否  | 根据数据 boolean 值，添加或移除当前标签  ## [#](span.html#样式) 样式
 | 名称  | 类型  | 默认值  | 必填  | 描述  | color  | ``  | rgba(0, 0, 0, 0.54)  | 否  | 文本颜色  | font-size  | ``  | 30px  | 否  | 文本尺寸  | font-style  | normal | italic  | normal  | 否  | -  | font-weight  | normal | bold | ``  | normal  | 否  | 当前平台仅支持`normal`与`bold`两种效果，当值为数字时，低于`550`为前者，否则为后者  | text-decoration  | underline | line-through | none  | none  | 否  | -  ## [#](span.html#事件) 事件
 不支持
 ## [#](span.html#示例代码) 示例代码
 ```

      I am span,
      I am span,
      I am span,

```
![](../../../images/components/span.png)


---
## 文件: components\basic\text.html

# # text

# [#](text.html#text) text
 ## [#](text.html#概述) 概述
 文本内容写在标签内容区，支持转义字符`"\"`。
 ## [#](text.html#子组件) 子组件
 仅支持``
 ## [#](text.html#属性) 属性
 支持[通用属性](../general/properties.html)
 ## [#](text.html#样式) 样式
 支持[通用样式](../general/style.html)
 | 名称  | 类型  | 默认值  | 必填  | 描述  | lines  | ``  | -1  | 否  | 文本行数，-1 代表不限定行数  | color  | ``  | rgba(0, 0, 0, 0.54)  | 否  | 文本颜色  | font-size  | ``  | 30px  | 否  | 文本尺寸  | font-style  | normal | italic  | normal  | 否  |   | font-weight  | normal | bold | ``  | normal  | 否  | 当前平台仅支持`normal`与`bold`两种效果，当值为数字时，低于`550`为前者，否则为后者  | text-decoration  | underline | line-through | none  | none  | 否  |   | text-align  | left | center | right  | left  | 否  |   | text-indent  | `` | ``  | -  | 否  | 规定文本块首行的缩进  | line-height  | ``  | -  | 否  | 文本行高  | text-overflow  | clip | ellipsis  | clip  | 否  | 在设置了行数的情况下生效  **示例**
 
- 单行省略
 ```
text {
  width: 150px;
  lines: 1;
  text-overflow: ellipsis;
}

```
![](../../../images/components/text-overflow.png)

 - 多行省略，以两行为例
 ```
text {
  width: 100px;
  lines: 2;
  text-overflow: ellipsis;
}

```
![](../../../images/components/text-overflow-2.png)

 ## [#](text.html#事件) 事件
 支持[通用事件](../general/events.html)
 ## [#](text.html#示例代码) 示例代码
 ```

    这是一段文本

```
![](../../../images/components/text-example.png)


---
## 文件: components\container\div.html

# # div

# [#](div.html#div) div
 ## [#](div.html#概述) 概述
 基础容器，用作页面结构的根节点或将内容进行分组。
 ## [#](div.html#子组件) 子组件
 支持
 ## [#](div.html#属性) 属性
 支持[通用属性](../general/properties.html)
 ## [#](div.html#样式) 样式
 支持[通用样式](../general/style.html)
 ## [#](div.html#事件) 事件
 支持[通用事件](../general/events.html)
 ## [#](div.html#示例代码) 示例代码
 ```

      1
      2
      3

  .page {
    margin: 20px;
    flex-direction: column;
    background-color: white;
  }

  .item {
    height: 100px;
    width: 100px;
    text-align: center;
    margin-right: 10px;
  }
  
  .color-1 {
    background-color: #09ba07;
  }
  
  .color-2 {
    background-color: #f76160;
  }
  
  .color-3 {
    background-color: #0faeff;
  }

```
![](../../../images/components/div.png)


---
## 文件: components\container\index.html

# # 容器组件

# [#](index.html#容器组件) 容器组件
 一般来说，指可以包含子组件的组件。
 
- [div](div.html)
 - [list](list.html)
 - [list-item](list-item.html)
 - [scroll](scroll.html)
 - [stack](stack.html)
 - [swiper](swiper.html)


---
## 文件: components\container\list-item.html

# # list-item

# [#](list-item.html#list-item) list-item
 ## [#](list-item.html#概述) 概述
 [``](list.html)的子组件，用来展示列表具体 item，宽度默认充满 list 组件。
 ## [#](list-item.html#子组件) 子组件
 支持
 ## [#](list-item.html#属性) 属性
 支持[通用属性](../general/properties.html)
 | 名称  | 类型  | 默认值  | 必填  | 描述  | type  | ``  | -  | 是  | list-item 类型，值为自定义的字符串，如'loadMore'。**相同的 type 的 list-item 必须具备完全一致的 DOM 结构**。因此，在 list-item 内部需谨慎使用 if 和 for，因为 if 和 for 可能造成相同的 type 的 list-item 的 DOM 结构不一致，从而引发错误  ## [#](list-item.html#样式) 样式
 支持[通用样式](../general/style.html)
 为了达到组件复用、优化性能的目的，请显示指定宽度和高度。
 ## [#](list-item.html#事件) 事件
 支持[通用事件](../general/events.html)
 ## [#](list-item.html#示例代码) 示例代码
 ```

        {{$item.name}}: {{$item.price}}

  export default {
    data: {
      productList: [
        { name: '衣服', price: '100' },
        { name: '裤子', price: '200' }
      ],
    }
  }

  .page {
    padding: 30px;
    background-color: white;
  }

  .list {
    width: 100%;
    height: 100%;
  }

  .item {
    height: 40px;
  }

```
### [#](list-item.html#效果展示) 效果展示
 ![](../../../images/components/list.png)


---
## 文件: components\container\list.html

# # list

# [#](list.html#list) list
 ## [#](list.html#概述) 概述
 列表视图容器，包含一系列相同结构的列表项，连续、多行呈现同类数据。
 ## [#](list.html#子组件) 子组件
 仅支持[``](list-item.html)
 ## [#](list.html#属性) 属性
 支持[通用属性](../general/properties.html)
 | 名称  | 类型  | 默认值  | 必填  | 描述  | bounces  | ``  | false  | 否  | 是否边界回弹  ## [#](list.html#样式) 样式
 支持[通用样式](../general/style.html)
 使用时需要显式地设置高度。
 ## [#](list.html#事件) 事件
 支持[通用事件](../general/events.html)
 | 名称  | 参数  | 描述  | scroll  | {scrollX: ``, scrollY: ``, scrollState: ``}  | 列表滑动；
stateValue 取值说明：
0：list停止滑动
1：list正在通过用户的手势滑动
2：list正在滑动，用户已松手  | scrollbottom  | -  | 列表滑动到底部  | scrolltop  | -  | 列表滑动到顶部  | scrollend  | -  | 列表滑动结束  | scrolltouchup  | -  | 列表滑动过程中手指抬起  ## [#](list.html#示例代码) 示例代码
 ```

    <list class="list" bounces="true" 
      onscroll="onScroll" 
      onscrolltop="onScrollTop" 
      onscrollbottom="onScrollBottom"
      onscrolltouchup="onScrollTouchup">
      
        {{$item.name}}: {{$item.price}}

  export default {
    private: {
      productList: [
        { name: '衣服', price: '100' },
        { name: '裤子', price: '200' },
        { name: '鞋子', price: '300' },
        { name: '帽子', price: '60' },
        { name: '雨伞', price: '300' },
        { name: '书包', price: '60' },
        { name: '书本', price: '30' }
      ],
    },
    onScroll(e) {
      console.log('### list onScroll evt: ', e)
    },
    onScrollTop(e) {
      console.log('### list onScrollTop evt: ', e)
    },
    onScrollBottom(e) {
      console.log('### list onScrollBottom evt: ', e)
    },
    onScrollTouchup(e) {
      console.log('### list onScrollTouchup evt: ', e)
    }
  }

  .page {
    justify-content: center;
    align-items: center;
    background-color: #000;
  }

  .list {
    width: 300px;
    height: 200px;
    border: 1px solid #fff;
  }

  text {
   color: #fff;
  }
  .item {
    height: 40px;
    width: 100%;
    align-items: center;
    justify-content: center;
    border: 1px solid #fff;
  }

```
### [#](list.html#效果展示) 效果展示
 ![](../../../images/components/list-methods.jpeg)
 ## [#](list.html#方法) 方法
 | 名称  | 参数  | 描述  | scrollTo  | Object  | list 滚动到指定 item 位置  | scrollBy  | Object  | 使 list 的内容滑动一定距离  **scrollTo 的参数说明：**
 | 名称  | 类型  | 默认值  | 必填  | 描述  | index  | number  | 0  | 否  | list 滚动的目标 item 位置  | behavior  | smooth / instant / auto  | auto  | 否  | 是否平滑滑动，支持参数 smooth (平滑滚动)，instant (瞬间滚动)，默认值 auto，效果等同于 instant  **scrollBy 的参数说明：**
 | 名称  | 类型  | 默认值  | 必填  | 描述  | left  | number  | 0  | 否  | 从当前位置水平方向滑动距离，单位 px。值为正时向左滑动，为负时向右滑动。flex-direction 为 column 或 column-reverse 时不生效  | top  | number  | 0  | 否  | 从当前位置垂直方向滑动距离，单位 px。值为正时向上滑动，为负时向下滑动。flex-direction 为 row 或 row-reverse 时不生效  | behavior  | smooth / instant / auto  | auto  | 否  | 是否平滑滑动，支持参数 smooth (平滑滚动)，instant (瞬间滚动)，默认值 auto，效果等同于 instant


---
## 文件: components\container\scroll.html

# # scroll2+

# [#](scroll.html#scroll) scroll[2+](../../guide/version/APILevel2.html)
 ## [#](scroll.html#概述) 概述
 滚动视图容器。竖向或水平方向滚动容器，竖向滚动需要设置定高，水平滚动需要设置定宽。
 ## [#](scroll.html#子组件) 子组件
 支持。也支持嵌套子 scroll。
 ## [#](scroll.html#属性) 属性
 支持[通用属性](../general/properties.html)
 | 名称  | 类型  | 默认值  | 必填  | 描述  | scroll-x  | ``  | false  | 否  | 是否允许横向滚动  | scroll-y  | ``  | false  | 否  | 是否允许纵向滚动  | scroll-top  | `` | ``  |   | 否  | 设置竖向滚动条位置，内容顶部到 scroll 顶部的距离，如果有滚动吸附效果则先滚动再吸附  | scroll-bottom  | `` | ``  |   | 否  | 设置竖向滚动条位置，内容底部到 scroll 底部的距离，如果有滚动吸附效果则先滚动再吸附。同时设置 scroll-top 和scroll-bottom 以scroll-top为准  | scroll-left  | `` | ``  |   | 否  | 设置横向滚动条位置，内容左侧到 scroll 左侧的距离，如果有滚动吸附效果则先滚动再吸附  | scroll-right  | `` | ``  |   | 否  | 设设置横向滚动条位置，内容右侧到 scroll 右侧的距离，如果有滚动吸附效果则先滚动再吸附。同时设置 scroll-left 和scroll-right 以scroll-left为准  | bounces  | ``  | false  | 否  | 是否边界回弹  ## [#](scroll.html#样式) 样式
 支持[通用样式](../general/style.html)
 | 名称  | 类型  | 默认值  | 描述  | scroll-snap-type[3+](../../guide/version/APILevel3.html)  | -  | none  | 与scroll-snap-align配合使用，作用在scroll组件上，表示scroll的滚动吸附类型。第一个参数为x或y，表示水平方向上滚动或竖直方向上滚动；第二个参数为 mandatory、proximity、 cross。mandatory：表示选择距离最近的锚点吸附；proximity：表示距离吸附锚点不到容器高度的 30% 时才会吸附；cross：表示子组件能够被吸附的边界出现在 scroll 视口内才会吸附。默认为 proximity 
 aiot-toolkit最低版本：1.1.4  | scroll-snap-align[3+](../../guide/version/APILevel3.html)  | none | start | center | end | edge  | none  | 与scroll-snap-type配合使用，作用在scroll子组件上，表示子组件和scroll的对⻬形式。none：表示无需对⻬，默认值；start：表示组件和scroll起始边对⻬；center：表示组件和scroll中心对⻬；end：表示组件和scroll终止边对⻬；edge：在滚动方向上，组件和 scroll 起始边或者结束边对齐 
 aiot-toolkit最低版本：1.1.4  | scroll-snap-stop[3+](../../guide/version/APILevel3.html)  | normal | always  | normal  | 值为 always 时不能跨越元素进行吸附 
 aiot-toolkit最低版本：1.1.4  ### [#](scroll.html#示例代码) 示例代码
 
- scroll-snap-type & scroll-snap-align```

        A
        B
        C
        D
        E
        F
        G
        H

  export default {}

  .page {
    padding: 60px;
    flex-direction: column;
  }

  .scroll-container {
    width: 100%;
  }

  .box {
    margin-bottom: 30px;
    height: 100px;
    width: 200px;
  }

  .scroll-item {
    width: 80%;
    height: 100px;
    text-align: center;
  }

  .color-1 {
    background-color: cadetblue;
  }

  .color-2 {
    background-color: orangered;
  }

```

 ![](../../../images/components/scroll.gif)
 
- scroll-snap-stop```

        A
        B
        C
        D
        E
        F
        G

  export default {}

  .page {
    padding: 60px;
    flex-direction: column;
  }

  .scroll-container {
    width: 100%;
  }

  .box {
    margin-bottom: 30px;
    height: 100px;
    width: 200px;
  }

  .scroll-item {
    width: 80%;
    height: 100px;
    text-align: center;
  }

  .color-1 {
    background-color: cadetblue;
  }

  .color-2 {
    background-color: orangered;
  }

```

 ![](../../../images/components/scroll-snap-stop.gif)
 ## [#](scroll.html#事件) 事件
 | 名称  | 参数  | 描述  | scrolltop  | -  | 滚动到顶部触发  | scrollbottom  | -  | 滚动到底部触发  | scroll  | { scrollX, scrollY }  | 滚动触发，scrollX 表示滚动的水平距离；scrollY 表示滚动的垂直距离  ## [#](scroll.html#方法) 方法
 | 名称  | 参数  | 返回值  | 描述  | getScrollRect  | 无  | ``  | 获取滚动内容的尺寸  | scrollTo  | Object  | 无  | 让滚动组件窗口滚动到某个坐标位置  | scrollBy  | Object  | 无  | 使滚动组件窗口滚动一定距离  ### [#](scroll.html#scrollto方法object参数) scrollTo方法Object参数
 | 名称  | 类型  | 默认值  | 必填  | 描述  | left  | number  | -  | 否  | 滚动组件的横轴坐标值，不传表示横轴不滚动，负数按0处理，超出滚动范围按滚动边界处理  | top  | number  | -  | 否  | 滚动组件的纵轴坐标值，不传表示纵轴不滚动，负数按0处理，超出滚动范围按滚动边界处理  | behavior  | smooth / instant / auto  | auto  | 否  | 滚动行为，smooth-平滑滚动，instant-瞬间滚动，auto-等同于instant  ### [#](scroll.html#scrollby方法object参数) scrollBy方法Object参数
 | 名称  | 类型  | 默认值  | 必填  | 描述  | left  | number  | -  | 否  | 滚动组件的横轴偏移量，可以是负数，超出滚动范围按滚动边界处理  | top  | number  | -  | 否  | 滚动组件的纵轴偏移量，可以是负数，超出滚动范围按滚动边界处理  | behavior  | smooth / instant / auto  | auto  | 否  | 滚动行为，smooth-平滑滚动，instant-瞬间滚动，auto-等同于instant  ### [#](scroll.html#返回值-异步) 返回值（异步）
 | 属性  | 类型  | 描述  | width  | ``  | 滚动内容的宽度，包含border和padding  | height  | ``  | 滚动内容的高度，包含border和padding  ## [#](scroll.html#示例代码-2) 示例代码
 ```

             北京

             上海

             广州

             深圳

  export default {
    onShow() {
      this.$element('scrollId').getScrollRect({
        success({ width, height }) {
          console.log('宽度', width);
          console.log('高度', height);  
        }
      })

      // this.scrollTo()
      // this.scrollBy()
    },
    handleScrollTop() {
      console.info('scrolled top.')
    },
    scrollTo() {
      this.$element('scrollId').scrollTo({
        top: 1000,
        left: 0,
        behavior: 'smooth'
      })
    },
    scrollBy() {
      this.$element('scrollId').scrollBy({
        top: 1000,
        left: 0,
        behavior: 'smooth'
      })
    }
  }

  .page {
    justify-content: center;
    align-items: center;
  }

  #scrollId {
    width: 50%;
    height: 100px;
    flex-direction: column;
    background-color: yellowgreen;
  }

  .item {
    width: 100%;
    height: 50px;
    justify-content: center;
  }

```


---
## 文件: components\container\stack.html

# # stack

# [#](stack.html#stack) stack
 ## [#](stack.html#概述) 概述
 基本容器，子组件排列方式为层叠排列，每个直接子组件按照先后顺序依次堆叠，覆盖前一个子组件。
 ## [#](stack.html#子组件) 子组件
 支持
 ## [#](stack.html#属性) 属性
 支持[通用属性](../general/properties.html)
 ## [#](stack.html#样式) 样式
 支持[通用样式](../general/style.html)
 ## [#](stack.html#事件) 事件
 支持[通用事件](../general/events.html)
 ## [#](stack.html#示例代码) 示例代码
 ```

  .page {
    padding: 30px;
    background-color: white;
  }

  .box {
    border-radius: 8px;
    width: 100px;
    height: 100px;
  }

  .box1 {
    width: 200px;
    height: 200px;
    background-color: #3f56ea;
  }

  .box2 {
    left: 20px;
    top: 20px;
    background-color: #00bfc9;
  }

  .box3 {
    left: 50px;
    top: 50px;
    background-color: #47cc47;
  }

  .box4 {
    left: 80px;
    top: 80px;
    background-color: #FF6A00;
  }

```
![](../../../images/components/stack.png)


---
## 文件: components\container\swiper.html

# # swiper

# [#](swiper.html#swiper) swiper
 ## [#](swiper.html#概述) 概述
 滑块视图容器。
 ## [#](swiper.html#子组件) 子组件
 支持
 ## [#](swiper.html#属性) 属性
 支持[通用属性](../general/properties.html)
 | 名称  | 类型  | 默认值  | 必填  | 描述  | index  | ``  | ０  | 否  | 当前显示的子组件索引  | autoplay  | ``  | false  | 否  | 渲染完成后，是否自动进行播放  | interval  | ``  | 3000ms  | 否  | 自动播放时的时间间隔，单位毫秒  | indicator  | ``  | true  | 否  | 是否启用 indicator，默认 true  | loop  | ``  | true  | 否  | 是否开启循环模式  | duration  | ``  | -  | 否  | 滑动动画时长（duration默认根据手指的速度动态计算）  | vertical  | ``  | false  | 否  | 滑动方向是否为纵向，纵向时indicator 也为纵向  | previousmargin  | ``  | 0px  | 否  | 前边距，可用于露出前一项的一小部分，支持单位：px和%  | nextmargin  | ``  | 0px  | 否  | 后边距，可用于露出后一项的一小部分，支持单位：px和%  | enableswipe  | ``  | true  | 否  | 是否支持手势滑动swiper  **备注**：`previousmargin`和`nextmargin`的总和不应该超过整个swiper大小的1/2，超过部分将会被截取。
 ## [#](swiper.html#样式) 样式
 支持[通用样式](../general/style.html)
 | 名称  | 类型  | 默认值  | 必填  | 描述  | indicator-color  | ``  | rgba(0, 0, 0, 0.5)  | 否  | indicator 填充颜色  | indicator-selected-color  | ``  | #33b4ff 或者 rgb(51, 180, 255)  | 否  | indicator 选中时的颜色  | indicator-size  | ``  | 20px  | 否  | indicator 组件的直径大小  | indicator-[top|left|right|bottom]  | `` | ``  | -  | 否  | indicator相对于swiper的位置  ## [#](swiper.html#事件) 事件
 支持[通用事件](../general/events.html)
 | 名称  | 参数  | 描述  | change  | {index:currentIndex}  | 当前显示的组件索引变化时触发  | swipestart[2+](../../guide/version/APILevel2.html)  | {index:currentIndex}  | 子组件切换动画开始时触发（如果是手指拖动导致的切换，指的是手指按压开始拖动的时间点）  | swipeend[2+](../../guide/version/APILevel2.html)  | {index:currentIndex}  | 子组件切换动画结束时触发  ## [#](swiper.html#方法) 方法
 | 名称  | 参数  | 描述  | swipeTo  | {index: number(指定位置)}  | swiper 滚动到 index 位置  ## [#](swiper.html#示例代码) 示例代码
 ```

      A
      B
      C
      D

  .page {
    padding: 30px;
    background-color: white;
  }

  .swiper {
    width: 300px;
    height: 160px;
    indicator-size: 10px;
  }

  .item {
    text-align: center;
    color: white;
    font-size: 30px;
  }

  .item-1 {
    background-color: #3f56ea;
  }

  .item-2 {
    background-color: #00bfc9;
  }

  .item-3 {
    background-color: #47cc47;
  }

  .item-4 {
    background-color: #FF6A00;
  }

```
![](../../../images/components/swiper.gif)


---
## 文件: components\form\index.html

# # 表单组件

# [#](index.html#表单组件) 表单组件
 用来进行交互的组件，如提交数据等。
 
- [input](input.html)
 - [picker](picker.html)
 - [switch](switch.html)
 - [slider](slider.html)


---
## 文件: components\form\input.html

# # input

# [#](input.html#input) input
 ## [#](input.html#概述) 概述
 提供可交互的界面，接收用户的输入。
 ## [#](input.html#子组件) 子组件
 不支持
 ## [#](input.html#属性) 属性
 支持[通用属性](../general/properties.html)
 | 名称  | 类型  | 默认值  | 必填  | 描述  | type  | button | checkbox | radio |  | button  | 否  | 支持动态修改  | checked  | ``  | false  | 否  | 当前组件的 checked 状态，type 为 checkbox 时生效，可触发 checked 伪类（checked 伪类样式还未支持）  | name  | ``  | -  | 否  | input 组件名称  | value  | ``  | -  | 否  | input 组件的值  ## [#](input.html#样式) 样式
 支持[通用样式](../general/style.html)
 | 名称  | 类型  | 默认值  | 必填  | 描述  | color  | ``  | rgba(0, 0, 0, 0.87)  | 否  | 文本颜色  | font-size  | ``  | 37.5px  | 否  | 文本尺寸  | width  | `` | ``  | -  | 否  | type 为 button 时，默认值为 128px  | height  | `` | ``  | -  | 否  | type 为 button 时，默认值为 70px  ## [#](input.html#事件) 事件
 支持[通用事件](../general/events.html)
 | 名称  | 参数  | 描述  | change  | 不同 type 参数不同，具体见下方 change 事件参数  | input 组件的值、状态发生改变时触发，type 为 button 时无 change 事件  ### [#](input.html#change-事件参数) change 事件参数
 | 参数  | checkbox  | radio  | 备注  | name  | √  | √  | -  | value  | √  | √  | -  | checked  | √  | -  | -  ## [#](input.html#方法) 方法
 | 名称  | 参数  | 描述  | focus  | {focus:true|false}，focus 不传默认为 true  | 使组件获得或者失去焦点，可触发 focus 伪类（focus 伪类样式还未支持）  ## [#](input.html#示例代码) 示例代码
 ```

      input-button 组件
      
      {{ buttonText }}

      input-checkbox 组件
      
      我的勾选状态: {{ checkboxChecked }}

      input-radio 组件

      当前选中第{{ radioValue }}个

  export default {
    private: {
      buttonText: '',
      checkboxChecked: true,
      radioValue: '1'
    },
    onTextChange(e) {
      this.textValue = e.value
    },
    onButtonClick() {
      this.buttonText = '按钮被点击了'
    },
    onCheckboxChange(e) {
      this.checkboxChecked = e.checked
    },
    onRadioChange(e) {
      this.radioValue = e.value
    }
  }

  .page {
    flex-direction: column;
    padding: 30px;
    background-color: #ffffff;
  }

  .section {
    flex-direction: column;
    margin-bottom: 30px;
  }

  .title {
    font-weight: bold;
  }

  .button {
    width: 140px;
    height: 50px;
    font-size: 25px;
    color: white;
  }

  .checkbox, .radio {
    width: 40px;
    height: 40px;
    margin-right: 10px;
  }

```
![](../../../images/components/input.gif)


---
## 文件: components\form\picker.html

# # picker

# [#](picker.html#picker) picker
 ## [#](picker.html#概述) 概述
 滚动选择器，目前支持两种选择器，普通选择器，时间选择器。默认为普通选择器。

 ## [#](picker.html#子组件) 子组件
 不支持
 ## [#](picker.html#属性) 属性
 支持[通用属性](../general/properties.html)
 **普通选择器**
 | 名称  | 类型  | 默认值  | 必填  | 描述  | type  | text  | -  | 是  | 不支持动态修改  | range  | `Array`  | -  | 否  | 选择器的取值范围  | selected  | ``  | 0  | 否  | 选择器的默认取值，取值为 range 的索引  **时间选择器**
 | 名称  | 类型  | 默认值  | 必填  | 描述  | type  | time  | -  | 是  | 不支持动态修改  | selected  | ``  | 当前时间  | 否  | 选择器的默认取值，格式为 hh:mm  ## [#](picker.html#样式) 样式
 支持[通用样式](../general/style.html)
 | 名称  | 类型  | 默认值  | 必填  | 描述  | color  | ``  | 主题色  | 否  | 候选项字体颜色  | font-size  | ``  | 30px  | 否  | 候选项字体尺寸，单位px  | selected-color  | ``  | #ffffff  | 否  | 选中项字体颜色  | selected-font-size  | ``  | 20px  | 否  | 选中项字体尺寸，单位px  | selected-background-color  | ``  | -  | 否  | 选中项背景颜色  ## [#](picker.html#事件) 事件
 **普通选择器**
 | 名称  | 参数  | 描述  | change  | {newValue:newValue, newSelected:newSelected}  | 滚动选择器选择值后确定时触发（newSelected 为索引）  **时间选择器**
 | 名称  | 参数  | 描述  | change  | {hour:hour, minute:minute}  | 滚动选择器选择值后确定时触发  ## [#](picker.html#示例代码) 示例代码
 ```

    普通picker
    <picker 
      class="picker" 
      type="text" 
      range="{{pickerList}}" 
      selected="1" 
      onchange="onPickerChange">
    
    选择的值：{{v1}}

    时间picker
    <picker 
      class="picker" 
      type="time"
      selected="12:00" 
      onchange="onTimePickerChange">
    
    选择的值：{{v2}}

  export default {
    private: {
      pickerList: ['apple', 'peach', 'pear', 'banana'],
      v1: 'peach',
      v2: '12:00'
    },
    onPickerChange(e) {
      this.v1 = e.newValue;
    },
    onTimePickerChange(e) {
      this.v2 = e.hour + ':' + e.minute;
    }
  }

  .page {
    flex-direction: column;
    padding: 30px;
    background-color: #ffffff;
  }

  .title {
    font-weight: bold;
    color: #000;
  }

  .value {
    margin-top: 5px;
    margin-bottom: 30px;
    color: #090;
  }

  .picker {
    font-size: 25px;
    color: #000;
    selected-font-size: 30px;
    selected-color: #09f;
    selected-background-color: #ccc;
  }

```
![](../../../images/components/picker.gif)


---
## 文件: components\form\slider.html

# # slider

# [#](slider.html#slider) slider
 ## [#](slider.html#概述) 概述
 滑动选择器
 ## [#](slider.html#子组件) 子组件
 不支持
 ## [#](slider.html#属性) 属性
 支持[通用属性](../general/properties.html)
 | 名称  | 类型  | 默认值  | 必填  | 描述  | min  | ``  | ０  | 否  | -  | max  | ``  | 100  | 否  | -  | step  | ``  | 1  | 否  | -  | value  | ``  | 0  | 否  | -  ## [#](slider.html#样式) 样式
 支持[通用样式](../general/style.html)
 | 名称  | 类型  | 默认值  | 必填  | 描述  | color  | ``  | #f0f0f0 或者 rgb(240, 240, 240)  | 否  | 背景条颜色  | selected-color  | ``  | #009688 或者 rgb(0, 150, 136)  | 否  | 已选择颜色  | block-color  | ``  | -  | 否  | 滑块的颜色  | padding-[left|right]  | ``  | 32px  | 否  | 左右边距  ## [#](slider.html#事件) 事件
 支持[通用事件](../general/events.html)
 | 名称  | 参数  | 描述  | change  | {progress:progressValue, isFromUser:isFromUserValue}  | 完成一次拖动后触发的事件 
 isFromUser说明：
 该事件是否由于用户拖动触发  ## [#](slider.html#示例代码) 示例代码
 ```

    slider 组件
    
    slider的值：{{ sliderValue }}

  export default {
    private: {
      initialSliderValue: 10,
      sliderValue: null
    },
    onSliderChange (e) {
      this.sliderValue = e.progress
    }
  }

  .page {
    flex-direction: column;
    padding: 30px;
    background-color: #ffffff;
  }

  .title {
    font-weight: bold;
  }

  .slider {
    margin-top: 20px;
    margin-bottom: 20px;
    padding-left: 0;
    padding-right: 0;
  }

```
![](../../../images/components/slider.gif)


---
## 文件: components\form\switch.html

# # switch

# [#](switch.html#switch) switch
 ## [#](switch.html#概述) 概述
 开关选择
 ## [#](switch.html#子组件) 子组件
 不支持
 ## [#](switch.html#属性) 属性
 支持[通用属性](../general/properties.html)
 | 名称  | 类型  | 默认值  | 必填  | 描述  | checked  | ``  | false  | 否  | 可触发 checked 伪类（checked 伪类样式还未支持）  ## [#](switch.html#样式) 样式
 支持[通用样式](../general/style.html)
 | 名称  | 类型  | 默认值  | 必填  | 描述  | thumb-color  | ``  | #ffffff 或者 rgb(255, 255, 255)  | 否  | 滑块颜色  | track-color  | ``  | #0d84ff 或者 rgb(13, 132, 255)  | 否  | 滑轨颜色  ## [#](switch.html#事件) 事件
 支持[通用事件](../general/events.html)
 | 名称  | 参数  | 描述  | change  | {checked:checkedValue}  | checked 状态改变时触发  ## [#](switch.html#示例代码) 示例代码
 ```

    switch 组件
    
    状态：{{ switchValue }}

  export default {
    private: {
      switchValue: true
    },
    onSwitchChange(e) {
      this.switchValue = e.checked
    }
  }

  .page {
    flex-direction: column;
    padding: 30px;
    background-color: #ffffff;
  }

  .title {
    font-weight: bold;
  }

  .switch {
    width: 100px;
    margin-top: 10px;
  }

```
![](../../../images/components/switch.gif)


---
## 文件: components\general\animation-style.html

# # 动画样式

# [#](animation-style.html#动画样式) 动画样式
 Vela JS 应用支持开发者制作动画，提供了`transform`类、`transform-origin`类、`animation`类与`transition`类的动画样式属性，且参数格式与 CSS 对齐，更方便开发者上手适配动画。
 `transform`可参考此[文档  (opens new window)](https://developer.mozilla.org/zh-CN/docs/Web/CSS/transform)。
 `transform-origin`可参考此[文档  (opens new window)](https://developer.mozilla.org/zh-CN/docs/Web/CSS/transform-origin)。
 `animation`可参考此[文档  (opens new window)](https://developer.mozilla.org/zh-CN/docs/Web/CSS/animation)。
 `transition` 可参考此[文档  (opens new window)](https://developer.mozilla.org/zh-CN/docs/Web/CSS/transition)。
 ## [#](animation-style.html#动画样式列表) 动画样式列表
 | 名称  | 类型  | 默认值  | 描述  | transform  | ``  | -  | 见下面 transform 操作  | transform-origin  | ``  | -  | 见下面 transform-origin 操作  | animation-name  | ``  | -  | 与@keyframes 的 name 相呼应，见下面@keyframes 属性  | animation-delay  | ``  | 0  | 目前支持的单位为[ s(秒) | ms(毫秒) ]  | animation-duration  | ``  | 0  | 目前支持的单位为[ s(秒) | ms(毫秒) ]  | animation-iteration-count  | `` | `infinite`  | 1  | 定义动画播放的次数，可设置为`infinite`无限次播放  | animation-timing-function  | linear | ease | ease-in | ease-out | ease-in-out | cubic-bezier(``, ``, ``, ``) | step-start | step-end | steps(number_of_steps[, step-direction]?)  | ease  | -  | transition-property  | ``  | all  | 指定执行 transition 效果的通用样式属性名称，参见[详情](animation-style.html#transition-property-%E6%94%AF%E6%8C%81%E7%9A%84%E9%80%9A%E7%94%A8%E6%A0%B7%E5%BC%8F%E5%B1%9E%E6%80%A7)  | transition-duration  | ``  | 0s  | 指定 transition 执行时间  | transition-timing-function  | linear | ease | ease-in | ease-out | ease-in-out | cubic-bezier(``, ``, ``, ``) | step-start | step-end | steps(number_of_steps[, step-direction]?)  | ease  | 指定 transition 执行时的时间函数。该参数释义与 animation 相同  | transition-delay  | ``  | 0s  | 指定 transition 开始执行的时间，即当改变元素属性值后多长时间开始执行 transition 效果  **注**：
 
- animation-timing-function 类型

 cubic-bezier(``, ``, ``, ``) | step-start | step-end | steps(number_of_steps[, step-direction]?)其中：
 steps(number_of_steps，step-direction)
 | 名称  | 类型  | 默认值 
  | 必填 
  | 描述  | number_of_steps  | ``  | -  | 是  | 表示等间隔步数的正整数  | step-direction  | jump-start | jump-end | jump-none | jump-both | start | end  | end  | 否  | 指示函数左连续或右连续的关键字  
- cubic-bezier(x1, y1, x2, y2)

 参数 x1, y1, x2, y2 是 `` 类型的值，代表当前定义的立方贝塞尔曲线中的 P1 和 P2 点的横坐标和纵坐标，x1 和 x2 必须在 [0，1] 范围内，否则当前值无效。
 ## [#](animation-style.html#transform-操作) transform 操作
 向元素应用 2D 转换。该属性允许我们对元素进行旋转、缩放、移动。
支持的样式属性列表如下：
 | 名称  | 类型  | translate  | `` | ``  | translateX  | `` | ``  | translateY  | `` | ``  | scale  | ``  | scaleX  | ``  | scaleY  | ``  | rotate  | ``  ## [#](animation-style.html#transform-origin-操作) transform-origin 操作
 更改一个元素变形的原点，目前支持改变元素的 X 和 Y 轴。
 **注意：**
 
- 使用此属性必须先使用 transform 属性。

 示例代码：
 ```
/* 使用 % 值 */
div {
  transform: rotate(30deg); 
  transform-origin: 20% 40%;
}
/* 使用 px 值 */
div {
  transform: rotate(30deg); 
  transform-origin: 100px 100px;
}

```
## [#](animation-style.html#animation-name-属性) animation-name 属性
 指定所采用的一系列动画，属性值的每个名称代表一个由 @keyframes 属性定义的关键帧序列。该属性支持在组件中应用单个动画或多个动画 `1070+` ，应用多个动画时动画同时开始执行。
 示例代码：
 ```
/* 单个动画 */
animation-name: Color;
animation-name: translate;
animation-name: rotate;

/* 多个动画 1070+ */
animation-name: Color, Opacity;
animation-name: Width, translate, rotate;

```
## [#](animation-style.html#keyframes-属性) @keyframes 属性
 | 名称  | 类型  | 默认值
  | 描述  | background-color  | ``  | -  | -  | background-position  | `` |``| left | right | top | bottom | center  | 0px 0px  | 描述了背景图片在容器中绘制的位置，支持 1-4 个参数，详情见[背景图样式](background-img-styles.html)  | opacity  | ``  | -  | -  | width/height  | ``  | -  | 暂不支持百分比  | transform  | ``  | -  | 可以对元素进行旋转、缩放、移动  **注**：
 暂时不支持起始值(0%)或终止值(100%)缺省的情况，都需显式指定。
 ## [#](animation-style.html#transition-过渡动画) transition 过渡动画
 transition 过渡动画是实现动画的另一种方式。过渡动画可以为元素定义在不同状态之间切换时的过渡效果，比如通过 JavaScript 实现的状态变化。
 ### [#](animation-style.html#transition-使用示例) transition 使用示例
 共 4 个样式属性：transition-property、transition-duration、transition-timing-function、transition-delay，直接写在样式当中，使用示例如下：
 ```

  export default {
    data: {
      otherClass: ""
    },
    onShow() {
      const that = this
      setTimeout(() => {
        that.otherClass = "new-width"
      }, 1000);
    }
  };

.page {
  padding: 60px;
  align-items: center;
}
.div {
  width: 100px;
  height: 200px;
  background-color: red;
  transition-property: width;
  transition-duration: 2000ms;
  transition-timing-function: ease-in;
  transition-delay: 500ms;
}
.new-width {
  width: 300px;
}

```
上述 4 个样式属性可简写到一个中，表示当触发 div 的 width 变化后 0.5s，以加速的方式变化至新的 width 值，过渡动画持续 2s：
 ```
.div {
  transition: width 2000ms ease-in 500ms;
}

```
### [#](animation-style.html#transition-property-支持的通用样式属性) transition-property 支持的通用样式属性
 JS 应用中 transition-property 支持的通用样式属性列表如下：
 | 样式名称  | 备注  | width  | √  | height  | √  | opacity  | √  | visibility  | √  | color  | 暂不支持  | transform-origin  | 暂不支持  | transform  | 暂不支持  | padding  | 暂不支持  | padding-[left|top|right|bottom]  | 暂不支持  | margin  | 暂不支持  | margin-[left|top|right|bottom]  | 暂不支持  | border  | 暂不支持  | border-[left|top|right|bottom]  | 暂不支持  | border-width  | √  | border-[left|top|right|bottom]-width  | 暂不支持  | border-color  | √  | border-[left|top|right|bottom]-color  | 暂不支持  | border-radius  | 暂不支持  | border-[top|bottom]-[left|right]-radius  | 暂不支持  | background  | 暂不支持  | background-color  | √  | background-size  | 暂不支持  | background-position  | √  | flex  | 暂不支持  | flex-grow  | 暂不支持  | flex-shrink  | 暂不支持  | flex-basis  | 暂不支持  | [left|top|right|bottom]  | 暂不支持


---
## 文件: components\general\background-img-styles.html

# # 背景图样式

# [#](background-img-styles.html#背景图样式) 背景图样式
 当需要往页面组件内添加一个图片作为组件背景的时候，开发者可以对这个图片背景的大小、重复放置的模式、放置位置进行调整。
 ## [#](background-img-styles.html#background-size-属性) background-size 属性
 该属性定义了背景图片的大小。
 参数的个数可以是一个或两个。
 有效参数列表如下：
 | 参数  | 描述  | `contain`  | 等比例缩放背景图片以完全装入容器，可能容器部分显示空白（仅作为单一参数）  | `cover`  | 等比例缩放背景图片以完全覆盖容器，可能背景图片部分不可见（仅作为单一参数）  | `auto`  | 表示保持原图的尺寸不变。注意，此时原图的尺寸单位为物理分辨率，与手机屏幕分辨率单位一致，非JS 应用内的`1px`长  | ``  | 描述图片的具体尺寸，单位：px或dp，不支持浮点计算设置浮点值会被向下取整  | ``  | 描述图片的尺寸占容器对应方向尺寸的百分比，不支持浮点计算设置浮点值会被向下取整  当参数为两个的时候，第一个参数默认解析为水平轴的宽，第二个参数默认解析为竖直轴的高，如果只有一个参数，则将`auto`补充为第二个参数，然后按照双参数的规则解析。
 无效参数统一解析为默认值`auto`，即原图尺寸。
 **示例**
 ```

    图片大小 128 * 128
    背景容器 300 * 200

  .page {
    flex-direction: column;
    align-items: center;
    background-color: #000;
  }
  text {
    color: #fff;
    font-size: 24px;
  }
  .imgBg {
    width: 300px;
    height: 200px;
    margin-top: 20px;
    border: 2px solid yellowgreen;
    background-color: yellowgreen;
    background-image: url('../../common/logo.png');
    background-size: 300px 200px;
    background-repeat: no-repeat; // 暂未支持，以防支持之后样式显示异常建议加上
  }

```
**效果**
 ![](../../../images/components/background-size.jpeg) ## [#](background-img-styles.html#background-repeat-属性-暂未实现) background-repeat 属性（暂未实现）
 该属性定义了背景图片在组件中的重复方式，背景图片可以沿着水平轴、竖直轴、两个轴重复，或者不重复。
 参数的个数为一个。
 有效参数列表如下：
 | 参数  | 描述  | `repeat`  | 在水平轴和竖直轴上同时重复绘制图片  | `repeat-x`  | 只在水平轴上重复绘制  | `repeat-y`  | 只在竖直轴上重复绘制  | `no-repeat`  | 不会重复绘制图片  无效参数会被解析为默认值，即`repeat`。
 **示例**
 ```

  .container {
    width: 365px;
    height: 365px;
    background-color: #c7c7c7;
  }
  .img {
    width: 100%;
    height: 100%;
    background-image: url('../common/logo.png');
    /* 等比例缩放背景图片到宽度为组件宽的一半 */
    background-size: 50%;
    /* 在水平方向和竖直方向上重复绘制 */
    background-repeat: repeat;
    /* 背景图片处于组件中央 */
    background-position: center;
  }

```
```
.img {
  width: 100%;
  height: 100%;
  background-image: url('../common/logo.png');
  /* 等比例缩放背景图片到宽度为100px */
  background-size: 100px;
  /* 背景图片不重复绘制 */
  background-repeat: no-repeat;
  /* 背景图片距离组件左边缘20px，和上下边缘的距离比为3:7 */
  background-position: left 20px top 30%;
}

```
## [#](background-img-styles.html#background-position-属性) background-position 属性
 该属性定义了背景图片在组件中的位置。
 它可以使用1-4个值进行定义。如果使用两个非关键字值，第一个值表示水平位置，第二个值表示垂直位置。如果仅指定一个值，则第二个值默认是 center。如果使用三个或四个值，则长度百分比值是前面关键字值的偏移量。
 **一个值的语法：**
 值可能是：
 
- 关键字 `center`，用来居中背景图片。
 - 关键字 `top`、`left`、`bottom`、`right` 中的一个。用来指定把背景图放在哪一个边界。另一个维度被设置成50%，所以背景图在此维度居中显示。
 - `` 或 ``。指定相对于左边界的 x 坐标，y 坐标被设置成 50%，背景图在y轴居中。

 **两个值的语法：**
 一个定义 x 坐标，另一个定义 y 坐标。每个值可以是：
 
- 关键字 `top`、`left`、`bottom`、`right` 中的一个。如果这里给出 `left` 或 `right`，那么这个值定义 x 轴位置，另一个值定义 y 轴位置。如果这里给出 `top` 或 `bottom`，那么这个值定义 y 轴位置，另一个值定义 x 轴位置。
 - `` 或 ``。如果另一个值是 `left` 或 `right`，则该值定义相对于顶部边界的 Y。如果另一个值是 `top` 或 `bottom`，则该值定义相对于左边界的 X。如果两个值都是 `` 或 `` 值，则第一个定义 X，第二个定义 Y。

 ***注意：***
如果一个值是 `top` 或 `bottom`，那么另一个值不可能是 `top` 或 `bottom`。如果一个值是 `left` 或 `right`，那么另一个值不可能是 `left` 或 `right`。也就是说，例如，`top top` 和 `left right` 是无效的。
 ***排序：***
配对关键字时，位置并不重要，写成 `top left` 或 `left top` 其产生的效果是相同的。
使用 `` 或 `` 与关键字配对时顺序非常重要，定义 X 的值放在前面，然后是定义 Y 的值， `right 20px` 和 `20px right` 的效果是不相同的，前者有效但后者无效。`left 20%` 或 `20% bottom` 是有效的，因为 X 和 Y 值已明确定义且位置正确。
 ***默认值*** 是 `left top` 或者 `0% 0%`。
 **三个值的语法：**
 两个值是关键字值，第三个是前面值的偏移量：
 
- 第一个值是关键字 `top`、`left`、`bottom`、`right`，或者 `center`。如果设置为 `left` 或 `right`，则定义了 X。如果设置为 `top` 或 `bottom`，则定义了 Y，另一个关键字值定义了 X。
 - `` 或 ``，如果是第二个值，则是第一个值的偏移量。如果是第三个值，则是第二个值的偏移量。
 - 单个长度或百分比值是其前面的关键字值的偏移量。一个关键字与两个 `` 或 `` 值的组合无效。

 **四个值的语法：**
 第一个和第三个值是定义 X 和 Y 的关键字值。第二个和第四个值是前面 X 和 Y 关键字值的偏移量：
 
- 第一个值和第三个值是关键字值 `top`、`left`、`bottom`、 `right` 之一。如果设置为 `left` 或 `right`，则定义了 X。如果设置为 `top` 或 `bottom`，则定义了 Y，另一个关键字值定义了 X。
 - 第二个和第四个值是 `` 或 ``。第二个值是第一个关键字的偏移量。第四个值是第二个关键字的偏移量。

 无效参数全部解析为默认值（0px, 0px），即图片显示在组件的左上角。
 **示例**
 ```

    图片大小 128 * 128
    背景容器 300 * 200

  .page {
    flex-direction: column;
    align-items: center;
    background-color: #000;
  }
  text {
    color: #fff;
    font-size: 24px;
  }
  .imgBg {
    width: 300px;
    height: 200px;
    margin-top: 20px;
    border: 2px solid yellowgreen;
    background-color: yellowgreen;
    background-image: url('../../common/logo.png');
    background-size: cover;
    background-position: right bottom;
    background-repeat: no-repeat; // 暂未支持，以防支持之后样式显示异常建议加上
  }

```
**效果**
 ![](../../../images/components/background-position.jpeg) ## [#](background-img-styles.html#支持明细) 支持明细
 | 设备产品  | 说明  | 小米 S1 Pro 运动健康手表  | 不支持  | 小米手环 8 Pro  | 不支持  | 小米手环 9 / 9 Pro  | 不支持  | Xiaomi Watch S3  | 不支持  | Redmi Watch 4  | 不支持  | 小米腕部心电血压记录仪  | 不支持  | 小米手环 10  | 支持  | Xiaomi Watch S4  | 支持  | REDMI Watch 5  | 支持  | REDMI Watch 6  | 支持


---
## 文件: components\general\color.html

# # 颜色配置

# [#](color.html#颜色配置) 颜色配置
 Vela JS 应用支持 `rgb()` and `rgba()` 颜色值设置，
 开发者可参考[MDN 文档  (opens new window)](https://developer.mozilla.org/zh-CN/docs/Web/CSS/color_value)了解更多颜色值的信息。
 ## [#](color.html#颜色值格式示例) 颜色值格式示例
 
- `'#f0f'` (#rgb)
 - `'#ff00ff'` (#rrggbb)
 - `'rgb(255, 0, 255)'`
 - `'rgba(255, 255, 255, 1.0)'`
 - `'#f0ff'` (#rgba)
 - `'#ff00ff00'` (#rrggbbaa)

 ## [#](color.html#透明) 透明
 `rgba(0,0,0,0)`还有另外一个简写版本：
 
- `'transparent'`

 ## [#](color.html#颜色名字) 颜色名字
 你也可以用下面的颜色配置：
 
- ** aliceblue (#f0f8ff)
 - ** antiquewhite (#faebd7)
 - ** aqua (#00ffff)
 - ** aquamarine (#7fffd4)
 - ** azure (#f0ffff)
 - ** beige (#f5f5dc)
 - ** bisque (#ffe4c4)
 - ** black (#000000)
 - ** blanchedalmond (#ffebcd)
 - ** blue (#0000ff)
 - ** blueviolet (#8a2be2)
 - ** brown (#a52a2a)
 - ** burlywood (#deb887)
 - ** cadetblue (#5f9ea0)
 - ** chartreuse (#7fff00)
 - ** chocolate (#d2691e)
 - ** coral (#ff7f50)
 - ** cornflowerblue (#6495ed)
 - ** cornsilk (#fff8dc)
 - ** crimson (#dc143c)
 - ** cyan (#00ffff)
 - ** darkblue (#00008b)
 - ** darkcyan (#008b8b)
 - ** darkgoldenrod (#b8860b)
 - ** darkgray (#a9a9a9)
 - ** darkgreen (#006400)
 - ** darkgrey (#a9a9a9)
 - ** darkkhaki (#bdb76b)
 - ** darkmagenta (#8b008b)
 - ** darkolivegreen (#556b2f)
 - ** darkorange (#ff8c00)
 - ** darkorchid (#9932cc)
 - ** darkred (#8b0000)
 - ** darksalmon (#e9967a)
 - ** darkseagreen (#8fbc8f)
 - ** darkslateblue (#483d8b)
 - ** darkslategrey (#2f4f4f)
 - ** darkturquoise (#00ced1)
 - ** darkviolet (#9400d3)
 - ** deeppink (#ff1493)
 - ** deepskyblue (#00bfff)
 - ** dimgray (#696969)
 - ** dimgrey (#696969)
 - ** dodgerblue (#1e90ff)
 - ** firebrick (#b22222)
 - ** floralwhite (#fffaf0)
 - ** forestgreen (#228b22)
 - ** fuchsia (#ff00ff)
 - ** gainsboro (#dcdcdc)
 - ** ghostwhite (#f8f8ff)
 - ** gold (#ffd700)
 - ** goldenrod (#daa520)
 - ** gray (#808080)
 - ** green (#008000)
 - ** greenyellow (#adff2f)
 - ** grey (#808080)
 - ** honeydew (#f0fff0)
 - ** hotpink (#ff69b4)
 - ** indianred (#cd5c5c)
 - ** indigo (#4b0082)
 - ** ivory (#fffff0)
 - ** khaki (#f0e68c)
 - ** lavender (#e6e6fa)
 - ** lavenderblush (#fff0f5)
 - ** lawngreen (#7cfc00)
 - ** lemonchiffon (#fffacd)
 - ** lightblue (#add8e6)
 - ** lightcoral (#f08080)
 - ** lightcyan (#e0ffff)
 - ** lightgoldenrodyellow (#fafad2)
 - ** lightgray (#d3d3d3)
 - ** lightgreen (#90ee90)
 - ** lightgrey (#d3d3d3)
 - ** lightpink (#ffb6c1)
 - ** lightsalmon (#ffa07a)
 - ** lightseagreen (#20b2aa)
 - ** lightskyblue (#87cefa)
 - ** lightslategrey (#778899)
 - ** lightsteelblue (#b0c4de)
 - ** lightyellow (#ffffe0)
 - ** lime (#00ff00)
 - ** limegreen (#32cd32)
 - ** linen (#faf0e6)
 - ** magenta (#ff00ff)
 - ** maroon (#800000)
 - ** mediumaquamarine (#66cdaa)
 - ** mediumblue (#0000cd)
 - ** mediumorchid (#ba55d3)
 - ** mediumpurple (#9370db)
 - ** mediumseagreen (#3cb371)
 - ** mediumslateblue (#7b68ee)
 - ** mediumspringgreen (#00fa9a)
 - ** mediumturquoise (#48d1cc)
 - ** mediumvioletred (#c71585)
 - ** midnightblue (#191970)
 - ** mintcream (#f5fffa)
 - ** mistyrose (#ffe4e1)
 - ** moccasin (#ffe4b5)
 - ** navajowhite (#ffdead)
 - ** navy (#000080)
 - ** oldlace (#fdf5e6)
 - ** olive (#808000)
 - ** olivedrab (#6b8e23)
 - ** orange (#ffa500)
 - ** orangered (#ff4500)
 - ** orchid (#da70d6)
 - ** palegoldenrod (#eee8aa)
 - ** palegreen (#98fb98)
 - ** paleturquoise (#afeeee)
 - ** palevioletred (#db7093)
 - ** papayawhip (#ffefd5)
 - ** peachpuff (#ffdab9)
 - ** peru (#cd853f)
 - ** pink (#ffc0cb)
 - ** plum (#dda0dd)
 - ** powderblue (#b0e0e6)
 - ** purple (#800080)
 - ** rebeccapurple (#663399)
 - ** red (#ff0000)
 - ** rosybrown (#bc8f8f)
 - ** royalblue (#4169e1)
 - ** saddlebrown (#8b4513)
 - ** salmon (#fa8072)
 - ** sandybrown (#f4a460)
 - ** seagreen (#2e8b57)
 - ** seashell (#fff5ee)
 - ** sienna (#a0522d)
 - ** silver (#c0c0c0)
 - ** skyblue (#87ceeb)
 - ** slateblue (#6a5acd)
 - ** slategray (#708090)
 - ** snow (#fffafa)
 - ** springgreen (#00ff7f)
 - ** steelblue (#4682b4)
 - ** tan (#d2b48c)
 - ** teal (#008080)
 - ** thistle (#d8bfd8)
 - ** tomato (#ff6347)
 - ** turquoise (#40e0d0)
 - ** violet (#ee82ee)
 - ** wheat (#f5deb3)
 - ** white (#ffffff)
 - ** whitesmoke (#f5f5f5)
 - ** yellow (#ffff00)
 - ** yellowgreen (#9acd32)


---
## 文件: components\general\events.html

# # 通用事件

# [#](events.html#通用事件) 通用事件
 通用事件，即所有组件都支持的`事件回调`。
 开发者可以在组件标签上使用`on{eventName}`（如`onclick`）或`@{eventName}`（如`@click`）注册回调事件。
 更详细的讲解，可查阅[事件绑定](../../guide/framework/template/event.html)文档了解。
 ## [#](events.html#示例代码) 示例代码
 ```

      line 1
      line 2

```
## [#](events.html#通用事件列表) 通用事件列表
 | 名称  | 参数  | 描述  | 冒泡  | touchstart  | TouchEvent  | 手指刚触摸组件时触发  | 支持  | touchmove  | TouchEvent  | 手指触摸后移动时触发  | 支持  | touchend  | TouchEvent  | 手指触摸动作结束时触发  | 支持  | click  | MouseEvent  | 组件被点击时触发  | 支持  | longpress  | MouseEvent  | 组件被长按时触发  | 支持  | swipe  | { direction:  }  | 组件上快速滑动后触发（滑动方向有滚动条时不触发该事件）
 参数说明：
 left：　向左滑动；
right： 向右滑动；
up：　 向上滑动；
down：向下滑动；  | 不支持  ## [#](events.html#事件对象) 事件对象
 ### [#](events.html#_1、touchevent-类型说明) 1、TouchEvent 类型说明：
 | 属性  | 类型  | 说明  | touches  | TouchList  | 触摸事件，当前停留在屏幕中的触摸点信息的数组  | changedTouches  | TouchList  | 触摸事件，当前变化的触摸点信息的数组。changedTouches 数据格式同 touches， 表示有变化的触摸点，如从无变有（touchstart），位置变化（touchmove），从有变无（touchend），
比如用户手指离开屏幕时，touches 数组中无数据，而 changedtouches 则会保存离开前的数据  **其中，TouchList 是 Touch 对象的集合。**
 ### [#](events.html#_2、touch-类型说明) 2、Touch 类型说明
 | 属性  | 类型  | 说明  | identifier  | number  | 触摸点的标识符。对于多点同时触摸则是 [0,1,2,3,...]，分别表示第一个手指、第二个手指...
一次触摸动作(手指按下到手指离开的过程)，在整个屏幕移动过程中，该标识符不变，可以用来判断是否是同一次触摸过程  | clientX  | number  | 距离可见区域左边沿的 X 轴坐标，不包含任何滚动偏移  | clientY  | number  | 距离可见区域上边沿的 Y 轴坐标，不包含任何滚动偏移以及状态栏和 titlebar 的高度  | pageX  | number  | 距离可见区域左边沿的 X 轴坐标，包含任何滚动偏移  | pageY  | number  | 距离可见区域上边沿的 Y 轴坐标，包含任何滚动偏移。（不包含状态栏和 titlebar 的高度）  | offsetX  | number  | 距离事件触发对象左边沿 X 轴的距离  | offsetY  | number  | 距离事件触发对象上边沿 Y 轴的距离  ### [#](events.html#_3、mouseevent-类型说明) 3、MouseEvent 类型说明
 | 属性  | 类型  | 说明  | clientX  | number  | 距离可见区域左边沿的 X 轴坐标，不包含任何滚动偏移  | clientY  | number  | 距离可见区域上边沿的 Y 轴坐标，不包含任何滚动偏移以及状态栏和 titlebar 的高度  | pageX  | number  | 距离可见区域左边沿的 X 轴坐标，包含任何滚动偏移  | pageY  | number  | 距离可见区域上边沿的 Y 轴坐标，包含任何滚动偏移。（不包含状态栏和 titlebar 的高度）  | offsetX  | number  | 距离事件触发对象左边沿 X 轴的距离  | offsetY  | number  | 距离事件触发对象上边沿 Y 轴的距离  ## [#](events.html#示例) 示例
 如下，在 div 上绑定了 click 和 touchmove 事件，触发事件时将事件打印出来。
 ```

  .root-page {
    flex-direction: column;
    align-items: center;
  }

  .touch-region {
    width: 80%;
    height: 20%;
    background-color: #444444;
  }

  export default {
    private: {},
    click(event) {
      console.log("click event fired")
    },
    move(event) {
      console.log("move event touches:" + JSON.stringify(event.touches))
      console.log("move event changedTouches:" + JSON.stringify(event.changedTouches))
    }
  }

```
**打印结果如下，click 事件：**
 ```
move event touches:[
  {
    "offsetX": 296,
    "identifier": 0,
    "offsetY": 113.48148345947266,
    "clientY": 113.48148345947266,
    "clientX": 360,
    "pageY": 113.48148345947266,
    "pageX": 360
  }
]

```
```
move event changedTouches:[
  {
    "offsetX": 296,
    "identifier": 0,
    "offsetY": 113.48148345947266,
    "clientY": 113.48148345947266,
    "clientX": 360,
    "pageY": 113.48148345947266,
    "pageX": 360
  }
]

```
```
click event fired

```


---
## 文件: components\general\index.html

# # 组件通用说明

# [#](index.html#组件通用说明) 组件通用说明
 本章节介绍所有组件通用的样式、属性、事件。


---
## 文件: components\general\methods.html

# # 通用方法

# [#](methods.html#通用方法) 通用方法
 通用方法，是所有组件都可以调用的方法。在组件使用id标记 id 属性后，开发者可通过this.$element('idName')获取 dom 节点，再调用通用方法。
 通过 this.$element 获取到的 dom 对象，提供两个 api 供调用：
 ### [#](methods.html#getboundingclientrect-object-object) getBoundingClientRect(Object object)[2+](../../guide/version/APILevel2.html)
 返回元素的大小及其相对于视窗的位置，需要在页面的 onShow 生命周期之后调用。
 #### [#](methods.html#参数) 参数
 Object object
 | 属性  | 类型  | 默认值  | 必填  | 描述  | success  | function  |   | 否  | 接口调用成功的回调函数  | fail  | function  |   | 否  | 接口调用失败的回调函数  | complete  | function  |   | 否  | 接口调用结束的回调函数（调用成功、失败都会执行）  #### [#](methods.html#object-success-回调函数参数说明) object.success 回调函数参数说明
 Object rect
 | 属性  | 类型  | 描述  | left  | number  | 元素的左边界坐标  | right  | number  | 元素的右边界坐标  | top  | number  | 元素的上边界坐标  | bottom  | number  | 元素的下边界坐标  | width  | number  | 元素的宽度  | height  | number  | 元素的高度  #### [#](methods.html#代码示例) 代码示例
 ```

  export default {
    onShow(){
      this.$element('box1').getBoundingClientRect({
        success: function(data) {
          const { top, bottom, left, right, width, height } = data;
          console.log(data);
        },
        fail: (errorData, errorCode) => {
          console.log(`错误原因：${JSON.stringify(errorData)}, 错误代码：${errorCode}`)
        },
        complete: function() {
          console.info('complete')
        }
      })
    }
  }

```
### [#](methods.html#focus-object-object) focus(Object object)
 使组件获得或者失去焦点的方法
 #### [#](methods.html#参数-2) 参数
 | 属性  | 类型  | 默认值  | 必填  | 描述  | focus  | boolean  | true  | 否  | 使组件获得或者失去焦点，聚焦时可触发 focus 伪类效果（focus 伪类样式还未支持）  #### [#](methods.html#代码示例-2) 代码示例
 ```

  // 聚焦效果
  this.$element('box1').focus();
  // 聚焦效果
  this.$element('box2').focus({focus:true});
  // 失焦效果
  this.$element('box3').focus({focus:false});

```


---
## 文件: components\general\properties.html

# # 通用属性

# [#](properties.html#通用属性) 通用属性
 通用属性，即所有组件都支持的属性。
 开发者可以在所有的组件标签上都使用`通用属性`。
 ## [#](properties.html#示例代码) 示例代码
 ```

      line 1
      line 2

```
## [#](properties.html#常规属性) 常规属性
 | 名称  | 类型  | 默认值  | 描述  | id  | ``  | -  | 唯一标识  | style  | ``  | -  | 样式声明  | class  | ``  | -  | 引用样式表  ## [#](properties.html#渲染属性) 渲染属性
 | 名称  | 类型  | 默认值  | 描述  | for  | ``  | -  | 根据数据列表，循环展开当前标签  | if  | ``  | -  | 根据数据 boolean 值，添加或移除当前标签  | show  | ``  | -  | 根据数据 boolean 值，显示或隐藏当前标签，相当于控制{ display: flex | none }  渲染属性工作方式详见[template 模板](../../guide/framework/template/index.html)。
 注意
 属性和样式不能混用，不能在属性字段中进行样式设置。

 ## [#](properties.html#data-属性) data 属性
 给组件绑定 data 属性，然后运行时通过 `dataset` 获取，方便进一步判断。
 **示例：**
 ```

  export default {
    onReady () {
      const el = this.$element('elNode1')
      const elData = el.dataset.personName
      console.info(`输出data属性： ${elData}`)
    }
  }

```


---
## 文件: components\general\style.html

# # 通用样式

# [#](style.html#通用样式) 通用样式
 通用样式，即所有组件都可以支持的样式。
 它们均与 css 的属性样式用法保持一致，开发者可写在`内联样式`或``标签里，实现组件样式的定制化。
 关于组件样式的设置，可以参考此[文档](../../guide/framework/style/page-style-and-layout.html)。
 ## [#](style.html#示例代码) 示例代码
 ```

  .page {
    padding: 30px;
    background-color: white;
  }

  .box-normal {
    background-color: #09ba07;
    width: 100px;
    height: 100px;
    border-radius: 8px;
    margin-right: 10px;
  }

```
![](../../../images/components/general-style.png)
 ## [#](style.html#属性列表) 属性列表
 **注**：通用样式均为非必填项。
 | 名称  | 类型  | 默认值  | 描述  | width  | `` | ``  | -  | 未设置时使用组件自身内容需要的宽度  | height  | `` | ``  | -  | 未设置时使用组件自身内容需要的高度  | min-width  | auto | `` | ``  | auto  | 指定元素的最小宽度。该属性不能为负值，默认值 `auto` 为弹性元素的默认最小宽度，下同  | min-height  | auto | `` | ``  | auto  | 指定元素的最小高度  | max-width  | none | `` | ``  | none  | 指定元素的最大宽度。该属性不能为负值，默认值 `none` 表示不做限制，下同  | max-height  | none | `` | ``  | none  | 指定元素的最大高度  | padding  | ``  | 0  | 简写属性，在一个声明中设置所有的内边距属性，该属性可以有 1 到 4 个值，具体请参考[MDN  (opens new window)](https://developer.mozilla.org/zh-CN/docs/Web/CSS/padding)文档  | padding-[left|top|right|bottom]  | ``  | 0  | 设置一个元素的某个方向的内边距，padding 区域指一个元素的内容和其边界之间的空间，该属性不能为负值  | margin  | ``  | 0  | 简写属性，在一个声明中设置所有的外边距属性，该属性可以有 1 到 4 个值，具体请参考[MDN  (opens new window)](https://developer.mozilla.org/zh-CN/docs/Web/CSS/margin)文档  | margin-[left|top|right|bottom]  | ``  | 0  | 设置一个元素的某个方向的外边距，该属性不能为负值  | border  | -  | 0  | 简写属性，在一个声明中设置所有的边框属性，可以按顺序设置属性 width style color，不设置的值为默认值  | border-style  | solid  | solid  | 暂时仅支持 1 个值，为元素的所有边框设置样式  | border-width  | ``  | 0  | 设置元素的所有边框宽度  | border-color  | ``  | black  | 设置元素的所有边框颜色，颜色值的填入请参考 [颜色配置](color.html)  | border-radius  | `` | ``  | 0  | border-radius 属性允许你设置元素的外边框圆角。设置时需要同时设置 border-width、border-color。radius 的幅度不会超过矩形较短边的一半  | background-color  | ``  | -  | 颜色值的填入请参考 [颜色配置](color.html)  | color  | ``  | -  | 颜色值的填入请参考 [颜色配置](color.html)  | background-image  | ``  | -  | 支持本地图片资源与网络图片资源；使用`internal://`协议图片需将aiot-toolkit升级到1.1.2以上版本  | background-size  | contain | cover | auto | `` | ``  | auto auto  | 设置背景图片大小，详情见[背景图样式](background-img-styles.html)  | background-repeat  | repeat | repeat-x | repeat-y | no-repeat  | repeat  | [暂不支持] 设置是否及如何重复绘制背景图像，详情见[背景图样式](background-img-styles.html)  | background-position  | `` |``| left | right | top | bottom | center  | 0px 0px  | 设置背景图片在容器中绘制的位置，支持 1-4 个参数，详情见[背景图样式](background-img-styles.html)  | box-shadow [3+](../../guide/version/APILevel3.html)  | `` `` `` |
 `` `` `` `` |
 `` `` `` `` ``
  | -  | 设置元素的阴影效果，该属性可设置的值包括阴影的 X 轴偏移量、Y 轴偏移量、模糊半径、扩散半径和[颜色](color.html)。
 写法举例：
 box-shadow: 60px -16px teal，值分别对应：x轴偏移量、y轴偏移量、[阴影颜色](color.html)；
 box-shadow: 10px 5px 5px black，值分别对应：x轴偏移量、y轴偏移量、阴影模糊半径、[阴影颜色](color.html)；
 box-shadow: 2px 2px 2px 1px rgba(0, 0, 0, 0.2)，值分别对应：x轴偏移量、y轴偏移量、阴影模糊半径、阴影扩散半径、[阴影颜色](color.html)  | opacity  | ``  | 1  | opacity 属性指定了一个元素的透明度  | display  | flex | none  | flex  | JS 应用只支持 flex 布局；将当前元素的 display 设置为 none JS 应用页面将不渲染此元素  | visibility  | visible | hidden  | visible  | visibility 属性控制显示或隐藏元素而不更改文档的布局  | flex  | ``  | -  | 父容器为`、`时生效  | flex-grow  | ``  | 0  | 父容器为`、`时生效  | flex-shrink  | ``  | 1  | 父容器为`、`时生效  | flex-basis  | ``  | -1  | 父容器为`、`时生效  | flex-direction  | ``  | row  | 默认为横向`row`，父容器为`、`时生效  | align-items  | ``  | flex-start  | align-items 定义了伸缩项目可以在伸缩容器的当前行的侧轴上对齐方式。flex-start(默认值)：伸缩项目在侧轴起点边的外边距紧靠住该行在侧轴起始的边。flex-end：伸缩项目在侧轴终点边的外边距靠住该行在侧轴终点的边 。center：伸缩项目的外边距盒在该行的侧轴上居中放置。baseline：伸缩项目根据他们的基线对齐。stretch：伸缩项目拉伸填充整个伸缩容器。此值会使项目的外边距盒的尺寸在遵照「min/max-width/height」属性的限制下尽可能接近所在行的尺寸  | justify-content  | ``  | flex-start  | justify-content 定义了伸缩项目沿着主轴线的对齐方式。flex-start(默认值)：伸缩项目向一行的起始位置靠齐。flex-end：伸缩项目向一行的结束位置靠齐。center：伸缩项目向一行的中间位置靠齐。space-between：伸缩项目会平均地分布在行里。第一个伸缩项目一行中的最开始位置，最后一个伸缩项目在一行中最终点位置。space-around：伸缩项目会平均地分布在行里，两端保留一半的空间  | position  | absolute | relative  | relative  | 支持 relative 和 absolute 属性值，且默认值为 relative；父容器为`、`时不生效  | [left|top|right|bottom]  | ``  | -  | 一般配合`absolute`布局使用，支持单位px，暂不支持百分比


---
## 文件: features\grammar.html

# # 通用语法

# [#](grammar.html#通用语法) 通用语法
 框架提供各种接口来获取应用的基本信息或者调用系统能力，每个接口包含若干 api 来执行具体的任务。接口使用前需要进行接口声明、模块导入，然后才能调用该接口下定义的若干 api。
 ## [#](grammar.html#接口声明) 接口声明
 在 manifest.json 文件的 features 字段下进行声明，例如：
 ```
[{ "name": "system.network" }]

```
## [#](grammar.html#导入模块) 导入模块
 使用接口前，需要在代码里导入接口模块，例如：
 ```
import network from '@system.network'
// 或
const network = require('@system.network')

```
## [#](grammar.html#接口-api-调用) 接口 api 调用
 接口提供的 api 的调用方式大概有以下几种：
 
- 同步 api
 - 异步 api
 - 订阅类 api

 ### [#](grammar.html#同步-api-调用) 同步 api 调用
 如果 api 没有返回值，一般会定义成同步 api，直接调用即可，例如：
 ```
audio.play()

```
### [#](grammar.html#异步-api-调用) 异步 api 调用
 如果 api 有返回值，一般会定义成异步 api 的形式，这类 api 除了普通参数，还有“success \ fail \ complete“这三个通用参数——分别是 api 执行“成功 \ 失败 \ 完成“后的回调方法，调用时可以传入这三个参数来获取 api 执行成功的返回值或进行执行失败 \ 完成后的处理，这三个通用参数的说明如下：
 | 名称  | 方法参数  | 参数类型  | 参数值  | 说明  | success  | data  | any  | api 执行的返回值，详见接口使用文档  | api 执行成功后触发  | fail  | data  | any  | api 执行错误信息内容，一般是字符串，也可能是其他类型，详见接口使用文档  | api 执行失败后触发  |   | code  | number  | api 执行错误码，详见[通用错误码](grammar.html#%E9%80%9A%E7%94%A8%E9%94%99%E8%AF%AF%E7%A0%81)  |   | complete  | -  | -  | -  | api 执行完成后触发  代码示例：
 ```
storage.get({
  key: 'A1',
  success: function(data) {
    console.log('handling success')
  },
  fail: function(data, code) {
    console.log(`handling fail, code = ${code}`)
  }
})

```
### [#](grammar.html#订阅-取消订阅-api) 订阅 / 取消订阅 api
 订阅类 api 不会立即返回结果，这类 api 需要开发者传入回调函数作为参数，该回调函数会在 api 完成时或者事件变化时被触发，可以执行多次。该通用回调函数参数说明如下：
 | 名称  | 方法参数  | 参数类型  | 参数值  | 说明  | success  | data  | any  | api 执行的返回值，详见接口使用文档  | api 调用成功或事件变更时触发，可能会触发多次  | fail  | data  | any  | 错误信息内容，一般是字符串，也可能是其他类型，详见接口使用文档  | api 执行失败时触发。一旦触发该回调函数，success不会再次被调用，接口调用结束  |   | code  | number  | api 执行错误码，详见[通用错误码](grammar.html#%E9%80%9A%E7%94%A8%E9%94%99%E8%AF%AF%E7%A0%81)  |   代码示例：
 ```
geolocation.subscribe({
  success: function(data) {
    console.log(
      `handling success: longitude = ${data.longitude}, latitude = ${
        data.latitude
      }`
    )
  },
  fail: function(data, code) {
    console.log(`handling fail, code = ${code}`)
  }
})

```
## [#](grammar.html#通用错误码) 通用错误码
 所有接口的 api 在执行出现错误时，会返回统一定义的通用错误码或者 api 自己定义的特殊错误码。这里对通用错误码进行说明如下：
 | code  | 定义  | 200  | 系统通用错误，所有系统未知异常发生时抛出。比如框架申请内存空间失败等  | 201  | 用户拒绝  | 202  | 参数错误，调用时未按照 api 定义进行正确的传参  | 203  | 该功能不支持  | 204  | 请求超时  | 205  | 重复提交  | 207  | 用户拒绝并选择不再询问  | 300  | I/O 错误


---
## 文件: features\index.html

# # 接口

# [#](index.html#接口) 接口
 框架提供若干接口供应用调用以实现更多功能，包括以下几种接口：
 
- 基本功能类接口：用以获取应用信息、平台信息、设备信息等；
 - 数据文件类接口：用以获取远程数据、上下载文件、本地数据存储等；
 - 系统能力接口：用来调用系统能力，例如获取网络状况、调节屏幕亮度等；
 - 媒体类接口：用来播放操作媒体文件，比如播放音乐文件等；

 每个接口称为一个 feature，每个 feature 包含若干个实现某具体功能的 api。


---
## 文件: features\basic\app.html

# # 应用上下文 app

# [#](app.html#应用上下文-app) 应用上下文 app
 ## [#](app.html#接口声明) 接口声明
 无需声明
 ## [#](app.html#导入模块) 导入模块
 ```
import app from '@system.app' 
// 或 
const app = require('@system.app')

```
## [#](app.html#接口定义) 接口定义
 ### [#](app.html#app-getinfo) app.getInfo()
 获取当前应用信息
 #### [#](app.html#参数) 参数：
 无
 #### [#](app.html#返回值) 返回值：
 | 参数名  | 类型  | 说明  | packageName  | String  | 应用包名  | icon  | String  | 应用图标路径  | name  | String  | 应用名称  | versionName  | String  | 应用版本名称  | versionCode  | Integer  | 应用版本号  | logLevel  | String  | log 级别  | source  | Object  | 应用来源  #### [#](app.html#source) source
 | 参数名  | 类型  | 说明  | packageName  | String  | 来源 app 的包名，一级来源  | type  | String  | 来源类型，二级来源，值为 shortcut、push、url、barcode、nfc、bluetooth、other  #### [#](app.html#示例) 示例：
 ```
console.log(JSON.stringify(app.getInfo()))

```
```
// console 值打印
{
  // 应用包名
  "packageName": "com.example.demo",
  // 应用名称
  "name": "demo",
  // 应用版本名称
  "versionName": "1.0.0",
  // 应用版本号
  "versionCode": 1,
  // 应用图片
  "icon": "/common/logo.png",
  // log 级别
  "logLevel": "debug",
  // 应用来源
  "source": {
    // 来源app的包名
    "packageName": "",
    // 来源类型
    "type": "shortcut"
  }
}

```
### [#](app.html#app-terminate) app.terminate()
 退出当前应用
 #### [#](app.html#参数-2) 参数：
 无
 #### [#](app.html#返回值-2) 返回值：
 无
 #### [#](app.html#示例-2) 示例：
 ```
app.terminate()

```
### [#](app.html#app-caniuse) app.canIUse()[3+](../../guide/version/APILevel3.html)
 #### [#](app.html#参数-3) 参数：
 | 类型  | 描述  | String  | 要查询的能力，格式见下方  #### [#](app.html#返回值-3) 返回值：
 | 类型  | 描述  | Boolean  | 查询的能力是否支持  ### [#](app.html#入参格式) 入参格式
 #### [#](app.html#查询接口) 查询接口
 ```
// 查询feature下的方法是否支持
'@${featureName}.${method}'
// 查询某个feature是否支持
'@${featureName}'

```
**示例**
 ```
import app from '@system.app';

if (app.canIUse('@system.router.push')) {
  // 可以使用方法@system.router.push
}
if (app.canIUse('@system.router')) {
  // 可以使用@system.router接口
}

```
#### [#](app.html#查询组件) 查询组件
 type取值可以是`'attr'`、`'style'`、`'method'`，分别对应组件的属性、样式、方法。
 ```
// 查询组件下的属性、样式、方法是否支持
`${componentName}.${type}.${name}`
// 查询组件是否支持
`${componentName}`

```
**示例**
 ```
import app from '@system.app';

if (app.canIUse('scroll')) {
  // 可以使用scroll组件
}
if (app.canIUse('scroll.attr.scroll-x')) {
  // 可以使用scroll组件的scroll-x属性
}

```


---
## 文件: features\basic\configuration.html

# # 应用配置 configuration

# [#](configuration.html#应用配置-configuration) 应用配置 configuration
 ## [#](configuration.html#接口声明) 接口声明
 无需声明
 ## [#](configuration.html#导入模块) 导入模块
 ```
import configuration from '@system.configuration' 
// 或 
const configuration = require('@system.configuration')

```
## [#](configuration.html#接口定义) 接口定义
 ### [#](configuration.html#configuration-getlocale) configuration.getLocale()
 获取应用当前的语言环境。默认使用系统的语言环境，会因为设置或系统语言环境改变而发生变化
 #### [#](configuration.html#参数) 参数：
 无
 #### [#](configuration.html#返回值) 返回值：
 | 参数名  | 类型  | 说明  | language  | String  | 语言  | countryOrRegion  | String  | 国家或地区  #### [#](configuration.html#示例) 示例：
 ```
const locale = configuration.getLocale()
console.log(locale.language)

```


---
## 文件: features\basic\device.html

# # 设备信息 device

# [#](device.html#设备信息-device) 设备信息 device
 ## [#](device.html#接口声明) 接口声明
 ```
{ "name": "system.device" }

```
## [#](device.html#导入模块) 导入模块
 ```
import device from '@system.device' 
// 或 
const device = require('@system.device')

```
## [#](device.html#接口定义) 接口定义
 ### [#](device.html#device-getinfo-object) device.getInfo(OBJECT)
 获取设备信息
 #### [#](device.html#参数) 参数：
 | 参数名  | 类型  | 必填  | 说明  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 执行结束后的回调  #### [#](device.html#success-返回值) success 返回值：
 | 参数值  | 类型  | 说明  | brand  | string  | 设备品牌  | manufacturer  | string  | 设备生产商  | model  | string  | 设备型号  | product  | string  | 设备代号  | osType  | string  | 操作系统名称  | osVersionName  | string  | 操作系统版本名称  | osVersionCode  | number  | 操作系统版本号  | platformVersionName  | string  | 运行平台版本名称  | platformVersionCode  | number  | 运行平台版本号  | language  | string  | 系统语言  | region  | string  | 系统地区  | APILevel[2+](../../guide/version/APILevel2.html)  | number  | 框架api版本  | screenWidth  | number  | 屏幕宽  | screenHeight  | number  | 屏幕高  | screenDensity[3+](../../guide/version/APILevel3.html)  | number  | 屏幕密度，即：设备像素比（device pixel ratio），是设备物理像素和逻辑像素（DP）的比值，其计算公式为：DPR = 设备 PPI / 160，PPI（pixels per inch）表示每英寸的像素数  | screenShape  | string  | 屏幕形状，可取值：rect 表示方形屏，circle 表示圆形屏，pill-shaped[3+](../../guide/version/APILevel3.html) 表示胶囊形屏  | deviceType[2+](../../guide/version/APILevel2.html)  | string  | 设备类型，可取值：watch、band、smartspeaker  #### [#](device.html#示例) 示例：
 ```
device.getInfo({
  success: function(ret) {
    console.log(`handling success， brand = ${ret.brand}`)
  }
})

```
### [#](device.html#device-getdeviceid-object) device.getDeviceId(OBJECT)
 获取设备唯一标识
 #### [#](device.html#权限要求) 权限要求
 获取设备信息
 开发者需要在 manifest.json 里面配置权限：
 ```
{
  "permissions": [
    { "name": "hapjs.permission.DEVICE_INFO" }
  ]
}

```
#### [#](device.html#参数-2) 参数：
 | 参数名  | 类型  | 必填  | 说明  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 执行结束后的回调  #### [#](device.html#success-返回值-2) success 返回值：
 | 参数值  | 类型  | 说明  | deviceId  | String  | 设备唯一标识  #### [#](device.html#示例-2) 示例：
 ```
device.getDeviceId({
  success: function (data) {
    console.log(`handling success: ${data.deviceId}`)
  },
  fail: function (data, code) {
    console.log(`handling fail, code = ${code}`)
  },
})

```
### [#](device.html#device-getserial-object) device.getSerial(OBJECT)
 获取设备序列号
 #### [#](device.html#权限要求-2) 权限要求
 获取设备信息
 开发者需要在 manifest.json 里面配置权限：
 ```
{
  "permissions": [
    { "name": "hapjs.permission.DEVICE_INFO" }
  ]
}

```
#### [#](device.html#参数-3) 参数：
 | 参数名  | 类型  | 必填  | 说明  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 执行结束后的回调  #### [#](device.html#success-返回值-3) success 返回值：
 | 参数值  | 类型  | 说明  | serial  | String  | 设备序列号  ```
device.getSerial({
    success: (data) => {
        console.log(`handling success: ${data.serial}`)
    },
    fail: (data, code) => {
        console.log(`handling fail, code = ${code}`)
    }
})

```
### [#](device.html#device-gettotalstorage-object) device.getTotalStorage(OBJECT)
 获取存储空间的总大小
 #### [#](device.html#参数-4) 参数：
 | 参数名  | 类型  | 必填  | 说明  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 执行结束后的回调  #### [#](device.html#success-返回值-4) success 返回值：
 | 参数值  | 类型  | 说明  | totalStorage  | Number  | 存储空间的总大小，单位是 Byte  ```
device.getTotalStorage({
    success: (data) => {
        console.log(`handling success: ${data.totalStorage}`)
    },
    fail: (data, code) => {
        console.log(`handling fail, code = ${code}`)
    }
})

```
### [#](device.html#device-getavailablestorage-object) device.getAvailableStorage(OBJECT)
 获取存储空间的可用大小
 #### [#](device.html#参数-5) 参数：
 | 参数名  | 类型  | 必填  | 说明  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 执行结束后的回调  #### [#](device.html#success-返回值-5) success 返回值：
 | 参数值  | 类型  | 说明  | availableStorage  | Number  | 存储空间的可用大小，单位是 Byte  ```
device.getAvailableStorage({
    success: (data) => {
        console.log(`handling success: ${data.availableStorage}`)
    },
    fail: (data, code) => {
        console.log(`handling fail, code = ${code}`)
    }
})

```


---
## 文件: features\basic\index.html

# # 基本功能

# [#](index.html#基本功能) 基本功能
 获取应用、平台、框架、系统基本信息，以及应用内页面导航。
 
- [应用上下文](app.html)
 - [设备信息](device.html)
 - [页面路由](router.html)
 - [应用配置](configuration.html)


---
## 文件: features\basic\router.html

# # 页面路由 router

# [#](router.html#页面路由-router) 页面路由 router
 ## [#](router.html#接口声明) 接口声明
 无需声明
 ## [#](router.html#导入模块) 导入模块
 ```
import router from '@system.router' 
// 或 
const router = require('@system.router')

```
## [#](router.html#接口定义) 接口定义
 ### [#](router.html#router-push-object) router.push(OBJECT)
 跳转到应用内的某个页面
 #### [#](router.html#参数) 参数：
 | 参数  | 类型  | 必填  | 说明  | uri  | String  | 是  | 要跳转到的 uri，可以是下面的格式：
1、包含 schema 的完整 uri；
2、以‘/’开头的应用内页面的路径；例：/about
3、以非‘/’开头的应用内页面的名称；例：About
4、特殊的，如果 uri 的值是"/"，则跳转到 path 为"/"的页，没有则跳转到首页

支持包含 schema 的完整 uri。对于带有 schema 的 uri，处理流程如下：
如果 schema 是 hap （参见 [hap 链接](../../guide/framework/other/hap-schema.html)），会跳转到 hap 链接所支持的类型  | params  | Object  | 否  | 跳转时需要传递的数据，参数可以在目标页面中通过`this.param1`的方式使用，param1 为 json 中的参数名，param1 对应的值会统一转换为 String 类型。使用`this.param1`变量时，需要在目标页面中在 `public`（应用外传参）或 `protected` (应用内传参)下定义 key 名相同的属性  #### [#](router.html#params参数) params参数：
 | 参数  | 类型  | 必填  | 说明  | ___PARAM_LAUNCH_FLAG___  | String  | 否  | JS 应用启动参数，目前仅支持"clearTask"，在启动目标页面时会清除除此页面外的其他页面。详见[页面启动模式](../../guide/framework/other/launch-mode.html)  #### [#](router.html#示例) 示例：
 
- 应用内切换页面
 
- path 切换```
router.push({
  uri: '/about',
  params: {
    testId: '1'
  }
})

```

 - name 切换```
// open page by name
router.push({
  uri: 'About',
  params: {
    testId: '1'
  }
})

```

 - 切换页面并清除其他页面```
router.push({
  uri: '/about',
  params: {
    ___PARAM_LAUNCH_FLAG___: 'clearTask'
  }
})

```

 ### [#](router.html#router-replace-object) router.replace(OBJECT)
 用应用内的某个页面替换当前页面，并销毁被替换的页面
 #### [#](router.html#参数-2) 参数：
 | 参数  | 类型  | 必填  | 说明  | uri  | String  | 是  | 要跳转到的 uri，可以是下面的格式：
1. 以"/"开头的应用内页面的路径；例：/about
1. 以非"/"开头的应用内页面的名称；例：About
1. 特殊的，如果 uri 的值是"/"，则跳转到 path 为"/"的页，没有则跳转到首页

  | params  | Object  | 否  | 跳转时需要传递的数据，参数可以在目标页面中通过`this.param1`的方式使用，param1 为 json 中的参数名，param1 对应的值会统一转换为 String 类型。使用`this.param1`变量时，需要在目标页面中在 `public`（应用外传参）或 `protected` (应用内传参)下定义 key 名相同的属性  #### [#](router.html#示例-2) 示例：
 ```
router.replace({
  uri: '/test',
  params: {
    testId: '1'
  }
})

```
### [#](router.html#router-back-object) router.back(OBJECT)
 返回指定页面
 #### [#](router.html#参数-3) 参数：
 | 参数  | 类型  | 必填  | 说明  | path  | String  | 否  | 返回目标页面的路径，可以是以下几种取值：
1. 不传该参数，返回上一页面
1. 以"/"开头的应用内已打开页面的路径；例：/about
1. 特殊的，如果 path 的值是"/"，则跳转到页面名称为"/"的页，没有则跳转到首页

注意点：
1. path 需要是以"/"开头的当前应用已经打开的页面路径，否则均视为无效参数，返回上一页面
1. 若根据 path 未匹配到已经打开的页面，返回上一页面
1. 若根据 path 参数匹配到多个页面，返回至最后打开的页面

  #### [#](router.html#示例-3) 示例：
 ```
// A页面, open page by name
router.push({
  uri: 'B'
})
// B页面, open page by name
router.push({
  uri: 'C'
})
// C页面, open page by name
router.push({
  uri: 'D'
})
// D页面, open page by name
router.push({
  uri: 'E'
})
// E页面不传入页面路径，返回至D页面
router.back()
// D页面不传入页面名称，返回至C页面
router.back()
// C页面传入页面路径，返回至A页面
router.back({
  path: '/A'
})

```
### [#](router.html#router-clear) router.clear()
 清空所有历史页面记录，仅保留当前页面
 #### [#](router.html#参数-4) 参数：
 无
 #### [#](router.html#示例-4) 示例：
 ```
router.clear()

```
### [#](router.html#router-getlength) router.getLength()
 获取当前页面栈的页面数量
 #### [#](router.html#返回值) 返回值:
 | 类型  | 说明  | Number  | 页面数量  #### [#](router.html#示例-5) 示例：
 ```
var length = router.getLength()
console.log(`page's length = ${length}`)

```
### [#](router.html#router-getstate) router.getState()
 获取当前页面状态
 #### [#](router.html#返回参数) 返回参数：
 | 参数名  | 类型  | 说明  | index  | Number  | 当前页面在页面栈中的位置  | name  | String  | 当前页面的名称  | path  | String  | 当前页面的路径  #### [#](router.html#示例-6) 示例：
 ```
var page = router.getState()
console.log(`page index = ${page.index}`)
console.log(`page name = ${page.name}`)
console.log(`page path = ${page.path}`)

```
### [#](router.html#router-getpages) router.getPages()
 获取当前页面栈列表
 #### [#](router.html#返回值-2) 返回值：
 | 类型  | 说明  | Array  | 页面栈列表。数组每一项都为 Object 类型  数组每一项构成：
 | 字段  | 类型  | 说明  | name  | String  | 页面的名称  | path  | String  | 页面的路径  #### [#](router.html#示例-7) 示例：
 ```
var stacks = router.getPages()
console.log('栈底页面名称为：', stacks[0].name) // 如 list、detail 等
console.log('栈底页面路径为：', stacks[0].path) // 如 /list、/detail、/home/preview

```


---
## 文件: features\data\file.html

# # 文件存储 file

# [#](file.html#文件存储-file) 文件存储 file
 ## [#](file.html#接口声明) 接口声明
 ```
{ "name": "system.file" }

```
## [#](file.html#导入模块) 导入模块
 ```
import file from '@system.file' 
// 或 
const file = require('@system.file')

```
## [#](file.html#接口定义) 接口定义
 ### [#](file.html#file-move-object) file.move(OBJECT)
 将源文件移动到指定位置，接口中使用的 URI 描述请参考[文件组织](../../guide/framework/project-structure.html#uri)
 #### [#](file.html#参数) 参数：
 | 参数名  | 类型  | 必填  | 说明  | srcUri  | String  | 是  | 源文件的 uri，不能是应用资源路径和 tmp 类型的 uri  | dstUri  | String  | 是  | 目标文件的 uri，不能是应用资源路径和 tmp 类型的 uri  | success  | Function  | 否  | 成功回调，返回目标文件的 uri  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 执行结束后的回调  #### [#](file.html#fail-返回错误代码) fail 返回错误代码：
 | 错误码  | 说明  | 202  | 参数错误  | 300  | I/O 错误  #### [#](file.html#示例) 示例：
 ```
file.move({
  srcUri: 'internal://cache/path/to/file',
  dstUri: 'internal://files/path/to/file',
  success: function(uri) {
    console.log(`move success: ${uri}`)
  },
  fail: function(data, code) {
    console.log(`handling fail, code = ${code}`)
  }
})

```
### [#](file.html#file-copy-object) file.copy(OBJECT)
 将源文件复制一份并存储到指定位置，接口中使用的 URI 描述请参考[文件组织](../../guide/framework/project-structure.html#uri)
 #### [#](file.html#参数-2) 参数：
 | 参数名  | 类型  | 必填  | 说明  | srcUri  | String  | 是  | 源文件的 uri  | dstUri  | String  | 是  | 目标文件的 uri，不能是应用资源路径和 tmp 类型的 uri  | success  | Function  | 否  | 成功回调，返回目标文件的 uri  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 执行结束后的回调  #### [#](file.html#fail-返回错误代码-2) fail 返回错误代码：
 | 错误码  | 说明  | 202  | 参数错误  | 300  | I/O 错误  #### [#](file.html#示例-2) 示例：
 ```
file.copy({
  srcUri: 'internal://cache/path/to/file',
  dstUri: 'internal://files/path/to/file',
  success: function(uri) {
    console.log(`copy success: ${uri}`)
  },
  fail: function(data, code) {
    console.log(`handling fail, code = ${code}`)
  }
})

```
### [#](file.html#file-list-object) file.list(OBJECT)
 获取指定目录下的文件列表，接口中使用的 URI 描述请参考[文件组织](../../guide/framework/project-structure.html#uri)
 #### [#](file.html#参数-3) 参数：
 | 参数名  | 类型  | 必填  | 说明  | uri  | String  | 是  | 目录 uri  | success  | Function  | 否  | 成功回调，返回{fileList:[{uri:'file1', lastModifiedTime:1234456, length:123456} ...]}  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 执行结束后的回调  #### [#](file.html#success-返回值) success 返回值：
 | 参数名  | 类型  | 说明  | fileList  | Array  | 文件列表，每个文件的格式为{uri:'file1', lastModifiedTime:1234456, length:123456}  #### [#](file.html#每个文件的元信息) 每个文件的元信息：
 | 参数名  | 类型  | 说明  | uri  | String  | 文件的 uri，该 uri 可以被其他组件或 Feature 访问  | length  | Number  | 文件大小，单位 B  | lastModifiedTime  | Number  | 文件的保存是的时间戳，从 1970/01/01 00:00:00 GMT 到当前时间的毫秒数  #### [#](file.html#fail-返回错误代码-3) fail 返回错误代码：
 | 错误码  | 说明  | 202  | 参数错误  | 300  | I/O 错误  #### [#](file.html#示例-3) 示例：
 ```
file.list({
  uri: 'internal://files/movies/',
  success: function(data) {
    console.log(data.fileList)
  },
  fail: function(data, code) {
    console.log(`handling fail, code = ${code}`)
  }
})

```
### [#](file.html#file-get-object) file.get(OBJECT)
 获取本地文件的文件信息，接口中使用的 URI 描述请参考[文件组织](../../guide/framework/project-structure.html#uri)
 #### [#](file.html#参数-4) 参数：
 | 参数名  | 类型  | 必填  | 说明  | uri  | String  | 是  | 文件的 uri，不能是应用资源路径和 tmp 类型的 uri  | recursive  | Boolean  | 否  | 是否递归获取子目录文件列表。默认 false  | success  | Function  | 否  | 成功回调，返回{uri:'file1', length:123456, lastModifiedTime:1233456}  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 执行结束后的回调  #### [#](file.html#success-返回值-2) success 返回值：
 | 参数名  | 类型  | 说明  | uri  | String  | 文件的 uri，该 uri 可以被其他组件或 Feature 访问  | length  | Number  | 文件大小，单位 B。当 type = dir 时，返回0  | lastModifiedTime  | Number  | 文件的保存时的时间戳，从 1970/01/01 08:00:00 到当前时间的毫秒数  | type  | String  | 文件类型，dir：目录；file：文件  | subFiles  | Array  | 当 type = dir 时，返回目录中的文件列表，recursive 为 true 时，同时返回其子目录中的文件信息  #### [#](file.html#fail-返回错误代码-4) fail 返回错误代码：
 | 错误码  | 说明  | 202  | 参数错误  | 300  | I/O 错误  #### [#](file.html#示例-4) 示例：
 ```
file.get({
  uri: 'internal://files/path/to/file',
  success: function(data) {
    console.log(data.uri)
    console.log(data.length)
    console.log(data.lastModifiedTime)
  },
  fail: function(data, code) {
    console.log(`handling fail, code = ${code}`)
  }
})

```
### [#](file.html#file-delete-object) file.delete(OBJECT)
 删除本地存储的文件，接口中使用的 URI 描述请参考[文件组织](../../guide/framework/project-structure.html#uri)
 #### [#](file.html#参数-5) 参数：
 | 参数名  | 类型  | 必填  | 说明  | uri  | String  | 是  | 需要删除的文件 uri，不能是应用资源路径和 tmp 类型的 uri  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 执行结束后的回调  #### [#](file.html#fail-返回错误代码-5) fail 返回错误代码：
 | 错误码  | 说明  | 202  | 参数错误  | 300  | I/O 错误  #### [#](file.html#示例-5) 示例：
 ```
file.delete({
  uri: 'internal://files/path/to/file',
  success: function(data) {
    console.log('handling success')
  },
  fail: function(data, code) {
    console.log(`handling fail, code = ${code}`)
  }
})

```
### [#](file.html#file-writetext-object) file.writeText(OBJECT)
 写文本到文件
 注意
 当您使用文件写入接口时，请务必注意及时清理无用的文件，特别是在 IoT 设备内存较小的情况下，可以避免内存过载和应用崩溃的问题。

 #### [#](file.html#参数-6) 参数：
 | 参数名  | 类型  | 必填  | 说明  | uri  | String  | 是  | 本地文件路径，不支持资源文件路径和 tmp 分区，如果文件不存在会创建文件  | text  | String  | 是  | 需要写入的字符串  | encoding  | String  | 否  | 编码格式，默认 UTF-8  | append  | Boolean  | 否  | 是否追加模式，默认 false  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 执行结束后的回调  #### [#](file.html#fail-返回错误代码-6) fail 返回错误代码：
 | 错误码  | 说明  | 202  | 参数错误  | 300  | I/O 错误  #### [#](file.html#示例-6) 示例：
 ```
file.writeText({
  uri: 'internal://files/work/demo.txt',
  text: 'test',
  success: function() {
    console.log('handling success')
  },
  fail: function(data, code) {
    console.log(`handling fail, code = ${code}`)
  }
})

```
### [#](file.html#file-writearraybuffer-object) file.writeArrayBuffer(OBJECT)
 写 Buffer 到文件
 注意
 当您使用文件写入接口时，请务必注意及时清理无用的文件，特别是在 IoT 设备内存较小的情况下，可以避免内存过载和应用崩溃的问题。

 #### [#](file.html#参数-7) 参数：
 | 参数名  | 类型  | 必填  | 说明  | uri  | String  | 是  | 本地文件路径，不支持资源文件路径和 tmp 分区，如果文件不存在会创建文件  | buffer  | Uint8Array  | 是  | 需要写入的 Buffer  | position  | Number  | 否  | 指向文件开始写入数据的位置的偏移量，默认 0  | append  | Boolean  | 否  | 是否追加模式，默认 false。当为 true 时，position 参数无效  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 执行结束后的回调  #### [#](file.html#fail-返回错误代码-7) fail 返回错误代码：
 | 错误码  | 说明  | 202  | 参数错误  | 300  | I/O 错误  #### [#](file.html#示例-7) 示例：
 ```
file.writeArrayBuffer({
  uri: 'internal://files/work/demo',
  buffer: buffer,
  success: function() {
    console.log('handling success')
  },
  fail: function(data, code) {
    console.log(`handling fail, code = ${code}`)
  }
})

```
### [#](file.html#file-readtext-object) file.readText(OBJECT)
 从文件中读取文本
 #### [#](file.html#参数-8) 参数：
 | 参数名  | 类型  | 必填  | 说明  | uri  | String  | 是  | 本地文件路径，支持应用资源路径，例如：'/Common/demo.txt'  | encoding  | String  | 否  | 编码格式，默认 UTF-8  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 执行结束后的回调  #### [#](file.html#success-返回值-3) success 返回值：
 | 参数名  | 类型  | 说明  | text  | String  | 读取的文本  #### [#](file.html#fail-返回错误代码-8) fail 返回错误代码：
 | 错误码  | 说明  | 202  | 参数错误  | 300  | I/O 错误  | 301  | 文件不存在  #### [#](file.html#示例-8) 示例：
 ```
file.readText({
  uri: 'internal://files/work/demo.txt',
  success: function(data) {
    console.log('text: ' + data.text)
  },
  fail: function(data, code) {
    console.log(`handling fail, code = ${code}`)
  }
})

```
### [#](file.html#file-readarraybuffer-object) file.readArrayBuffer(OBJECT)
 从文件中读取 Buffer
 #### [#](file.html#参数-9) 参数：
 | 参数名  | 类型  | 必填  | 说明  | uri  | String  | 是  | 本地文件路径，支持应用资源路径，例如：'/Common/demo.txt'  | position  | Number  | 否  | 读取的起始位置，默认值为文件的起始位置  | length  | Number  | 否  | 读取的长度，不填写则读取到文件结尾  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 执行结束后的回调  #### [#](file.html#success-返回值-4) success 返回值：
 | 参数名  | 类型  | 说明  | buffer  | Uint8Array  | 读取的文件内容  #### [#](file.html#fail-返回错误代码-9) fail 返回错误代码：
 | 错误码  | 说明  | 202  | 参数错误  | 300  | I/O 错误  | 301  | 文件不存在  #### [#](file.html#示例-9) 示例：
 ```
file.readArrayBuffer({
  uri: 'internal://files/work/demo',
  position: 100,
  length: 100,
  success: function(data) {
    console.log('buffer.length: ' + data.buffer.length)
  },
  fail: function(data, code) {
    console.log(`handling fail, code = ${code}`)
  }
})

```
### [#](file.html#file-access-object) file.access(OBJECT)
 判断文件或目录是否存在
 #### [#](file.html#参数-10) 参数：
 | 参数名  | 类型  | 必填  | 说明  | uri  | String  | 是  | 目录或文件 uri  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 执行结束后的回调  #### [#](file.html#fail-返回错误代码-10) fail 返回错误代码：
 | 错误码  | 说明  | 202  | 参数错误  | 300  | I/O 错误  #### [#](file.html#示例-10) 示例：
 ```
file.access({
  uri: 'internal://files/test',
  success: function(data) {
    console.log(`handling success`)
  },
  fail: function(data, code) {
    console.log(`handling fail, code = ${code}`)
  }
})

```
### [#](file.html#file-mkdir-object) file.mkdir(OBJECT)
 创建目录
 #### [#](file.html#参数-11) 参数：
 | 参数名  | 类型  | 必填  | 说明  | uri  | String  | 是  | 目录的 uri，不能是应用资源路径和 tmp 类型的 uri  | recursive  | Boolean  | 否  | 是否递归创建该目录的上级目录后再创建该目录。默认 false  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 执行结束后的回调  #### [#](file.html#fail-返回错误代码-11) fail 返回错误代码：
 | 错误码  | 说明  | 202  | 参数错误  | 300  | I/O 错误  #### [#](file.html#示例-11) 示例：
 ```
file.mkdir({
  uri: 'internal://files/dir/',
  success: function(data) {
    console.log(`handling success`)
  },
  fail: function(data, code) {
    console.log(`handling fail, code = ${code}`)
  }
})

```
### [#](file.html#file-rmdir-object) file.rmdir(OBJECT)
 删除目录
 #### [#](file.html#参数-12) 参数：
 | 参数名  | 类型  | 必填  | 说明  | uri  | String  | 是  | 目录的 uri，不能是应用资源路径和 tmp 类型的 uri  | recursive  | Boolean  | 否  | 是否递归删除子文件和子目录。默认 false  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 执行结束后的回调  #### [#](file.html#fail-返回错误代码-12) fail 返回错误代码：
 | 错误码  | 说明  | 202  | 参数错误  | 300  | I/O 错误  #### [#](file.html#示例-12) 示例：
 ```
file.rmdir({
  uri: 'internal://files/dir/',
  success: function(data) {
    console.log(`handling success`)
  },
  fail: function(data, code) {
    console.log(`handling fail, code = ${code}`)
  }
})

```


---
## 文件: features\data\index.html

# # 数据文件

# [#](index.html#数据文件) 数据文件
 提供本地数据存储和文件操作相关的能力。
 
- [数据存储](storage.html)
 - [文件存储](file.html)


---
## 文件: features\data\storage.html

# # 数据存储 storage

# [#](storage.html#数据存储-storage) 数据存储 storage
 ## [#](storage.html#接口声明) 接口声明
 ```
{ "name": "system.storage" }

```
## [#](storage.html#导入模块) 导入模块
 ```
import storage from '@system.storage' 
// 或 
const storage = require('@system.storage')

```
## [#](storage.html#方法) 方法
 ### [#](storage.html#storage-get-object) storage.get(OBJECT)
 读取存储内容
 #### [#](storage.html#参数) 参数：
 | 参数名  | 类型  | 必填  | 说明  | key  | String  | 是  | 索引  | default  | String  | 否  | 如果 key 不存在，返回 default。如果 default 未指定，返回长度为 0 的空字符串  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 执行结束后的回调  #### [#](storage.html#success-返回值) success 返回值：
 key 对应的存储内容
 #### [#](storage.html#示例) 示例：
 ```
storage.get({
  key: 'A1',
  success: function(data) {
    console.log('handling success')
  },
  fail: function(data, code) {
    console.log(`handling fail, code = ${code}`)
  }
})

```
### [#](storage.html#storage-set-object) storage.set(OBJECT)
 修改存储内容
 #### [#](storage.html#参数-2) 参数：
 | 参数名  | 类型  | 必填  | 说明  | key  | String  | 是  | 索引  | value  | String  | 否  | 新值。如果新值是长度为 0 的空字符串，会删除以 key 为索引的数据项  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 执行结束后的回调  #### [#](storage.html#示例-2) 示例：
 ```
storage.set({
  key: 'A1',
  value: 'V1',
  success: function(data) {
    console.log('handling success')
  },
  fail: function(data, code) {
    console.log(`handling fail, code = ${code}`)
  }
})

```
### [#](storage.html#storage-clear-object) storage.clear(OBJECT)
 清空存储内容
 #### [#](storage.html#参数-3) 参数：
 | 参数名  | 类型  | 必填  | 说明  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 执行结束后的回调  #### [#](storage.html#示例-3) 示例：
 ```
storage.clear({
  success: function(data) {
    console.log('handling success')
  },
  fail: function(data, code) {
    console.log(`handling fail, code = ${code}`)
  }
})

```
### [#](storage.html#storage-delete-object) storage.delete(OBJECT)
 删除存储内容
 #### [#](storage.html#参数-4) 参数：
 | 参数名  | 类型  | 必填  | 说明  | key  | String  | 是  | 索引  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 执行结束后的回调  #### [#](storage.html#示例-4) 示例：
 ```
storage.delete({
  key: 'A1',
  success: function(data) {
    console.log('handling success')
  },
  fail: function(data, code) {
    console.log(`handling fail, code = ${code}`)
  }
})

```


---
## 文件: features\network\fetch.html

# # 数据请求 fetch

# [#](fetch.html#数据请求-fetch) 数据请求 fetch
 ## [#](fetch.html#接口声明) 接口声明
 ```
{ "name": "system.fetch" }

```
## [#](fetch.html#导入模块) 导入模块
 ```
import fetch from '@system.fetch' 
// 或 
const fetch = require('@system.fetch')

```
## [#](fetch.html#接口定义) 接口定义
 ### [#](fetch.html#fetch-fetch-object) fetch.fetch(OBJECT)
 获取网络数据
 #### [#](fetch.html#参数) 参数：
 | 参数名  | 类型  | 必填  | 说明  | url  | String  | 是  | 资源 url  | data  | String/Object/ArrayBuffer  | 否  | 请求的参数，可以是字符串，或者是 js 对象、arraybuffer 对象。参考 `data与Content-Type关系` 部分  | header  | Object  | 否  | 请求的 header，会将其所有属性设置到请求的 header 部分。User-Agent 设置示例：{"Accept-Encoding": "gzip, deflate","Accept-Language": "zh-CN,en-US;q=0.8,en;q=0.6"}  | method  | String  | 否  | 默认为 GET，可以是：OPTIONS，GET，HEAD，POST，PUT，DELETE，TRACE，CONNECT  | responseType  | String  | 否  | 支持返回类型是 text，json，file，arraybuffer，默认会根据服务器返回 header 中的 Content-Type 确定返回类型，详见 `success返回值`  | success  | Function  | 否  | 成功返回的回调函数  | fail  | Function  | 否  | 失败的回调函数，可能会因为权限失败  | complete  | Function  | 否  | 结束的回调函数（调用成功、失败都会执行）  #### [#](fetch.html#data-与-content-type-关系) data 与 Content-Type 关系
 | data  | Content-Type  | 说明  | String  | 不设置  | Content-Type 默认为 text/plain，data 值作为请求的 body  | String  | 任意 Type  | data 值作为请求的 body  | Object  | 不设置  | Content-Type 默认为 application/x-www-form-urlencoded，data 按照 url 规则进行 encode 拼接作为请求的 body  | Object  | application/x-www-form-urlencoded  | data 按照 url 规则进行 encode 拼接作为请求的 body  | Object  | application/x-www-form-urlencoded 之外的任意 type  | -  | ArrayBuffer  | 不设置  | Content-Type 默认为 application/octet-stream，data 值作为请求的 body  | ArrayBuffer  | 任意 Type  | data 值作为请求的 body  #### [#](fetch.html#success-返回值) success 返回值：
 | 参数名  | 类型  | 说明  | code  | Integer  | 服务器状态 code  | data  | String/Object /ArrayBuffer  | 参考 `responseType与success中data关系` 部分  | headers  | Object  | 服务器 response 的所有 header  #### [#](fetch.html#responsetype-与-success-中-data-关系) responseType 与 success 中 data 关系：
 | responseType  | data  | 说明  | 无  | String  | 服务器返回的 header 中 type 是 text/*或 application/json、application/javascript、application/xml，值是文本内容，否则是存储的临时文件的 uri，临时文件如果是图片或者视频内容，可以将图片设置到 image  | text  | String  | 返回普通文本  | json  | Object  | 返回 js 对象  | file  | String  | 返回存储的临时文件的 uri  | arraybuffer  | ArrayBuffer  | 返回 ArrayBuffer 对象  #### [#](fetch.html#示例) 示例：
 ```
fetch.fetch({
  url: 'http://www.example.com',
  responseType: 'text',
  success: function(response) {
    console.log(`the status code of the response: ${response.code}`)
    console.log(`the data of the response: ${response.data}`)
    console.log(
      `the headers of the response: ${JSON.stringify(response.headers)}`
    )
  },
  fail: function(data, code) {
    console.log(`handling fail, errMsg = ${data}`)
    console.log(`handling fail, errCode = ${code}`)
  }
})

// 我们也可以使用promise的方式处理回调
fetch
  .fetch({
    url: 'http://www.example.com',
    responseType: 'text'
  })
  .then(res => {
    const result = res.data

    console.log(`the status code of the response: ${result.code}`)
    console.log(`the data of the response: ${result.data}`)
    console.log(
      `the headers of the response: ${JSON.stringify(result.headers)}`
    )
  })
  .catch(error => {
    console.log(`handling fail, errMsg = ${error.data}`)
    console.log(`handling fail, errCode = ${error.code}`)
  })

```
## [#](fetch.html#支持明细) 支持明细
 | 设备产品  | 说明  | 小米 S1 Pro 运动健康手表  | 支持  | 小米手环 8 Pro  | 不支持  | 小米手环 9 / 9 Pro  | 不支持  | Xiaomi Watch S3  | 支持  | Redmi Watch 4  | 不支持  | 小米腕部心电血压记录仪  | 不支持  | 小米手环 10  | 不支持  | Xiaomi Watch S4  | 支持  | REDMI Watch 5  | 支持  | REDMI Watch 6  | 支持


---
## 文件: features\network\index.html

# # 网络访问

# [#](index.html#网络访问) 网络访问
 提供远程数据请求和本地蓝牙连接设备之间通信的能力。
 
- [数据请求](fetch.html)
 - [设备通信](interconnect.html)
 - [下载](request.html)
 - [上传](uploadtask.html)


---
## 文件: features\network\interconnect.html

# # 设备通信 interconnect

# [#](interconnect.html#设备通信-interconnect) 设备通信 interconnect
 用于和搭配使用的手机 app 进行通信，收发手机 app 数据。
通信连接会自动建立，应用内不用关心连接的创建和销毁，但是可以注册回调函数来接收连接状态改变的信息，以便于进行相应处理，例如对用户进行提示。
 ## [#](interconnect.html#接口声明) 接口声明
 ```
{ "name": "system.interconnect" }

```
## [#](interconnect.html#导入模块) 导入模块
 ```
import interconnect from '@system.interconnect'
// 或
const interconnect = require('@system.interconnect')

```
## [#](interconnect.html#接口定义) 接口定义
 ### [#](interconnect.html#interconnect-instance) interconnect.instance()
 获取连接对象，在 app 中以单例形式存在，后续的数据收发都是基于这个连接对象
 #### [#](interconnect.html#参数) 参数：
 无
 #### [#](interconnect.html#返回值) 返回值：
 interconnect 的链接实例 connect 对象
 #### [#](interconnect.html#示例) 示例：
 ```
const connect = interconnect.instance()

```
### [#](interconnect.html#connect-getreadystate-object) connect.getReadyState(OBJECT)
 获取 App 连接状态
 #### [#](interconnect.html#object参数) OBJECT参数：
 | 参数名  | 类型  | 必填  | 说明  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  #### [#](interconnect.html#success-返回值) success 返回值：
 | 属性值  | 类型  | 说明  | status  | Number  | 1：连接成功，2：连接断开  #### [#](interconnect.html#fail-返回值) fail 返回值：
 | 参数值  | 类型  | 说明  | data  | String  | 错误信息  | code  | Number  | 错误码  #### [#](interconnect.html#错误码说明) 错误码说明：
 [支持通用错误码](../grammar.html#通用错误码)
 | 错误码  | 说明  | 1006  | 连接断开  #### [#](interconnect.html#示例-2) 示例：
 ```
connect.getReadyState({
  success: (data) => {
    if (data.status === 1) {
      console.log('连接成功')
    } else if (data.status === 2) {
      console.log('连接失败')
    }
  },
  fail: (data, code) => {
    console.log(`handling fail, code = ${code}`)
  }
})

```
### [#](interconnect.html#connect-diagnosis-object) connect.diagnosis(OBJECT)
 诊断手表应用和对端应用的连接情况，如果连接成功则返回ok，连接失败则返回失败原因。如果调用时正在连接则等待连接结束后再返回最终状态。
 #### [#](interconnect.html#obejct参数) Obejct参数：
 | 属性  | 类型  | 必填  | 说明  | timeout  | Number  | 否  | 等待诊断的超时时间，单位毫秒 
默认值：10000ms  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  #### [#](interconnect.html#success-返回值-2) success 返回值：
 | 属性值  | 类型  | 说明  | status  | Number  | 0: OK 链接成功
204：CONNECT_TIMEOUT 连接超时
1001：APP_UNINSTALLED 对端应用未安装
1000：OTHERS  其他链接错误  #### [#](interconnect.html#fail-返回值-2) fail 返回值：
 | 参数值  | 类型  | 说明  | data  | String  | 错误信息  | code  | Number  | 错误码  #### [#](interconnect.html#错误码说明-2) 错误码说明：
 [支持通用错误码](../grammar.html#通用错误码)
 #### [#](interconnect.html#示例-3) 示例：
 ```
connect.diagnosis({
  success: function (data) {
    console.log(`handling success, version = ${data.status}`)
  },
  fail: function (data, code) {
    console.log(`handling fail, code = ${code}`)
  },
})

```
### [#](interconnect.html#connect-send-object) connect.send(OBJECT)
 发送数据到手机 App 端
 #### [#](interconnect.html#object参数-2) Object参数：
 | 参数名  | 类型  | 必填  | 说明  | data  | Object  | 是  | 发送的数据  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  #### [#](interconnect.html#success-返回值-3) success 返回值：
 无
 #### [#](interconnect.html#fail-返回值-3) fail 返回值：
 | 参数值  | 类型  | 说明  | data  | String  | 错误信息  | code  | Number  | 错误码  #### [#](interconnect.html#错误码说明-3) 错误码说明：
 [支持通用错误码](../grammar.html#通用错误码)
 | 错误码  | 说明  | 204  | 链接超时  | 1006  | 连接断开  #### [#](interconnect.html#示例-4) 示例：
 ```
connect.send({
  data: {
    str: 'test',
    num: 123
  },
  success: ()=>{
    console.log(`handling success`)
  },
  fail: (data, code)=> {
    console.log(`handling fail, errMsg = ${data.data}, errCode = ${data.code}`)
  }
})

```
## [#](interconnect.html#事件) 事件
 ### [#](interconnect.html#connect-onmessage) connect.onmessage
 接收手机 App 端数据
 #### [#](interconnect.html#回调参数) 回调参数：
 | 参数名  | 类型  | 说明  | data  | String  | 接收的数据  #### [#](interconnect.html#示例-5) 示例：
 ```
connect.onmessage = (data) => {
  console.log(`received message: ${data.data}`)
}

```
### [#](interconnect.html#connect-onopen) connect.onopen
 连接打开时的回调函数
 #### [#](interconnect.html#回调参数-2) 回调参数：
 | 参数名  | 类型  | 说明  | isReconnected  | Boolean  | 是否是重新连接  #### [#](interconnect.html#示例-6) 示例：
 ```
connect.onopen = function (data) {
  console.log('connection opened isReconnected: ', data.isReconnected)
}

```
### [#](interconnect.html#connect-onclose) connect.onclose
 连接关闭时的回调函数
 #### [#](interconnect.html#回调参数-3) 回调参数：
 | 参数名  | 类型  | 说明  | code  | Number  | 链接关闭状态码  | data  | String  | 连接关闭返回的数据  #### [#](interconnect.html#示例-7) 示例：
 ```
connect.onclose = (data) => {
  console.log(`connection closed, reason = ${data.data}, code = ${data.code}`)
}

```
### [#](interconnect.html#connect-onerror) connect.onerror
 连接出错时的回调函数
 #### [#](interconnect.html#回调参数-4) 回调参数：
 | 参数名  | 类型  | 说明  | code  | Number  | 错误码，见错误码说明  | data  | String  | 错误信息  #### [#](interconnect.html#错误码说明-4) 错误码说明：
 [支持通用错误码](../grammar.html#通用错误码)
 | 错误码  | 说明  | 1000  | 未知错误  | 1001  | 手机 APP 未安装  | 1006  | 连接断开  #### [#](interconnect.html#示例-8) 示例：
 ```
connect.onerror = (data)=> {
  console.log(`connection error, errMsg = ${data.data}, errCode = ${data.code}`)
}

```
## [#](interconnect.html#开发注意事项) 开发注意事项
 interconnect 通信前提要保证快应用和三方应用安卓端两者的包名及签名保持一致。
 
- 保证快应用 manifest.json 里 package 字段与 需要接入的三方app 安卓端包名一致。
 - 快应用签名需要使用三方应用安卓端签名，可以从.jks中提取证书及私钥，方法如下：

1. 先将 jks 转换成 p12，执行以下命令，输入相应密码后，在同级目录下生成对应的 p12 格式文件。

 ```
keytool -importkeystore -srckeystore keystore.jks -destkeystore keystore.p12 -srcstoretype jks -deststoretype pkcs12

```

1. 再将 p12 转 pem，执行以下命令，输入上一步设置的 p12 文件密码后，在同级目录下生成对应的 pem 格式文件。

 ```
openssl pkcs12 -nodes -in keystore.p12 -out keystore.pem

```

1. 从 pem 格式文件中复制出私钥和证书：

把-----BEGIN PRIVATE KEY-----到-----END PRIVATE KEY-----的内容复制到private.pem中。

把-----BEGIN CERTIFICATE-----到-----END CERTIFICATE-----的内容复制到certificate.pem中。

- 如果本地没有安装Openssl或想要更简便的操作流程，我们提供了[在线签名生成工具  (opens new window)](https://cdn.hybrid.xiaomi.com/aiot-ide/signature-generate-tool/v2/index.html)。该工具是一个基于WebAssembly编写的Web应用，它可以在浏览器环境中直接生成 pem 格式的私钥和证书，无需将签名文件和密码上传到远程服务器，充分保证了用户的隐私安全。使用在线签名生成工具的步骤如下：
 
1. 上传 p12 文件并输入对应的密码；

 1. 点击“生成签名”按钮，等待签名生成成功弹窗出现；

 1. 点击“下载签名”按钮，下载 pem 格式的私钥和证书；

 - 快应用需要将上述生成的私钥  private.pem 和证书 certificate.pem 放在快应用根目录 /sign/debug 和 /sign/release 下出包测试。

 - 在真机测试的时候建议先输入包名uninstall老包再安装新包，可以观察桌面图标卸载的话会删除应用图标保证彻底替换。

 参考附录
 
1. 小米穿戴第三方APP能力开放接口文档：[点击下载  (opens new window)](https://vela-docs.cnbj1.mi-fds.com/vela-docs/files/%E5%B0%8F%E7%B1%B3%E7%A9%BF%E6%88%B4%E7%AC%AC%E4%B8%89%E6%96%B9APP%E8%83%BD%E5%8A%9B%E5%BC%80%E6%94%BE%E6%8E%A5%E5%8F%A3%E6%96%87%E6%A1%A3_1.4.pdf)
 1. interconnect开发测试demo：[点击下载  (opens new window)](https://cdn.cnbj3-fusion.fds.api.mi-img.com/quickapp-vela/interconnect_dev_test_demo.zip)


---
## 文件: features\network\request.html

# # 下载 request

# [#](request.html#下载-request) 下载 request
 ## [#](request.html#接口声明) 接口声明
 ```
{ "name": "system.request" }

```
## [#](request.html#导入模块) 导入模块
 ```
import request from '@system.request' 
// 或 
const request = require('@system.request')

```
## [#](request.html#接口定义) 接口定义
 ### [#](request.html#request-download-object) request.download(OBJECT)
 下载文件
 #### [#](request.html#参数) 参数：
 | 参数名  | 类型  | 必填  | 说明  | url  | String  | 是  | 资源 url  | header  | String  | 否  | 请求的 header，会将其所有属性设置到请求的 header 部分  | filename  | String  | 否  | 下载文件名。默认从网络请求或 url 中获取  | success  | Function  | 否  | 成功返回的回调函数  | fail  | Function  | 否  | 失败的回调函数  | complete  | Function  | 否  | 结束的回调函数（调用成功、失败都会执行）  #### [#](request.html#success-返回值) success 返回值：
 | 参数名  | 类型  | 说明  | token  | String  | 下载的 token，根据此 token 获取下载状态  #### [#](request.html#示例) 示例：
 ```
request.download({
  url: 'http://www.example.com',
  success: function(data) {
    console.log(`handling success${data.token}`)
  },
  fail: function(data, code) {
    console.log(`handling fail, code = ${code}`)
  }
})

```
### [#](request.html#request-ondownloadcomplete-object) request.onDownloadComplete(OBJECT)
 监听下载任务
 #### [#](request.html#参数-2) 参数：
 | 参数名  | 类型  | 必填  | 说明  | token  | String  | 是  | download 接口返回的 token  | success  | Function  | 否  | 成功返回的回调函数  | fail  | Function  | 否  | 失败的回调函数  | complete  | Function  | 否  | 结束的回调函数（调用成功、失败都会执行）  #### [#](request.html#success-返回值-2) success 返回值：
 | 参数名  | 类型  | 说明  | uri  | String  | 下载文件的 Uri（默认情况下该文件处于应用缓存目录。如果文件类型为图片或者视频且要求用户可以在相册等应用内查看，则需要将该文件转存至公共目录，参考media接口中的方法实现即可）  #### [#](request.html#fail-返回错误代码) fail 返回错误代码：
 | 错误码  | 说明  | 1000  | 下载失败  | 1001  | 下载任务不存在  #### [#](request.html#示例-2) 示例：
 ```
request.onDownloadComplete({
  token: '123',
  success: function(data) {
    console.log(`handling success${data.uri}`)
  },
  fail: function(data, code) {
    console.log(`handling fail, code = ${code}`)
  }
})

```
## [#](request.html#支持明细) 支持明细
 | 设备产品  | 说明  | 小米 S1 Pro 运动健康手表  | 支持  | 小米手环 8 Pro  | 不支持  | 小米手环 9 / 9 Pro  | 不支持  | Xiaomi Watch S3  | 支持  | Redmi Watch 4  | 不支持  | 小米腕部心电血压记录仪  | 不支持  | 小米手环 10  | 不支持  | Xiaomi Watch S4  | 支持  | REDMI Watch 5  | 支持  | REDMI Watch 6  | 支持


---
## 文件: features\network\uploadtask.html

# # 上传 uploadtask3+

# [#](uploadtask.html#上传-uploadtask) 上传 uploadtask[3+](../../guide/version/APILevel3.html)
 ## [#](uploadtask.html#接口声明) 接口声明
 ```
{ "name": "system.uploadtask" }

```
## [#](uploadtask.html#导入模块) 导入模块
 ```
import uploadtask from '@system.uploadtask' 
// 或 
const uploadtask = require('@system.uploadtask')

```
## [#](uploadtask.html#接口定义) 接口定义
 ### [#](uploadtask.html#方法) 方法
 ### [#](uploadtask.html#uploadtask-uploadtask-uploadfile-object) UploadTask uploadtask.uploadFile(OBJECT)
 创建一个上传请求，每次成功调用 uploadtask.uploadFile 将返回本次请求的 UploadTask 实例
 #### [#](uploadtask.html#参数) 参数：
 | 参数名  | 类型  | 必填  | 说明  | url  | String  | 是  | 开发者服务器接口地址  | filePath  | String  | 是  | 要上传文件资源的路径 (本地路径)  | name  | String  | 是  | 文件对应的 key，开发者在服务端可以通过这个 key 获取文件的二进制内容  | header  | Object  | 否  | 请求的 header，会将其所有属性设置到请求的 header 部分  | formData  | Object  | 否  | HTTP 请求中其他额外的 form data  | timeout  | Number  | 否  | 超时时间，单位为毫秒  | success  | Function  | 否  | 成功返回的回调函数  | fail  | Function  | 否  | 失败的回调函数  | complete  | Function  | 否  | 结束的回调函数（调用成功、失败都会执行）  #### [#](uploadtask.html#success-返回值) success 返回值：
 | 参数名  | 类型  | 说明  | statusCode  | Integer  | 服务器状态 code  | data  | String  | 开发者服务器返回的数据  | headers  | Object  | 服务器 response 的所有 header  # [#](uploadtask.html#uploadtask) UploadTask
 ## [#](uploadtask.html#方法-2) 方法
 ### [#](uploadtask.html#uploadtask-abort) UploadTask.abort()
 中断上传任务
 ### [#](uploadtask.html#uploadtask-onprogressupdate-callback) UploadTask.onProgressUpdate(callback)
 监听上传进度变化事件
 #### [#](uploadtask.html#参数-2) 参数：
 | 参数名  | 类型  | 必填  | 说明  | callback  | Function  | 是  | 上传进度变化事件的回调函数  #### [#](uploadtask.html#callback-返回值) callback 返回值：
 | 参数名  | 类型  | 说明  | progress  | Number  | 上传进度百分比  | totalBytesSent  | Number  | 已经上传的数据长度，单位 Bytes  | totalBytesExpectedToSend  | Number  | 预期需要上传的数据总长度，单位 Bytes  ### [#](uploadtask.html#uploadtask-offprogressupdate-function-callback) UploadTask.offProgressUpdate(function callback)
 取消监听上传进度变化事件。callback 是可选的，如果不传则取消所有通过 onProgressUpdate 监听的上传进度变化事件
 #### [#](uploadtask.html#示例) 示例：
 ```
const retUploadTask = uploadtask.uploadFile({
  url: 'http://www.example.com',
  filePath: "internal://mass/download/test.png",
  name: "testImg",
  success: function(res){
    console.log("Upload success.resp = " + JSON.stringify(res))
  },
  fail: function(data, code) {
    console.log(`handling fail, errMsg = ${data}`)
    console.log(`handling fail, errCode = ${code}`)
  }
})
// 中断请求任务
retUploadTask.abort()

// 监听上传进度事件
retUploadTask.onProgressUpdate(res => {
  console.log(
    `listening upload progress update event, progressUpdate data = ${JSON.stringify(res)}`
  )
})

// 取消监听上传进度事件
retUploadTask.offProgressUpdate()

```
取消特定的上传进度事件
 ```
function cb(res) {
  console.log(
    `listening for upload progress update event 1, progressUpdate data = ${JSON.stringify(
      res
    )}`
  )
}

// 此次监听会被取消
retUploadTask.onProgressUpdate(cb)

// event2 监听依然有效，不会被取消
retUploadTask.onProgressUpdate((res) => {
  console.log(
    `listening for upload progress update event 2, progressUpdate data = ${JSON.stringify(
      res
    )}`
  )
})

retUploadTask.offProgressUpdate(cb)

```
## [#](uploadtask.html#支持明细) 支持明细
 | 设备产品  | 说明  | 小米 S1 Pro 运动健康手表  | 不支持  | 小米手环 8 Pro  | 不支持  | 小米手环 9 / 9 Pro  | 不支持  | Xiaomi Watch S3  | 支持  | Redmi Watch 4  | 不支持  | 小米腕部心电血压记录仪  | 不支持  | 小米手环 10  | 不支持  | Xiaomi Watch S4  | 支持  | REDMI Watch 5  | 支持  | REDMI Watch 6  | 支持


---
## 文件: features\other\audio.html

# # 音频 audio

# [#](audio.html#音频-audio) 音频 audio
 ## [#](audio.html#接口声明) 接口声明
 ```
{ "name": "system.audio" }

```
## [#](audio.html#导入模块) 导入模块
 ```
import audio from '@system.audio' 
// 或 
const audio = require('@system.audio')

```
## [#](audio.html#方法) 方法
 ### [#](audio.html#audio-play) audio.play()
 开始播放音频
 #### [#](audio.html#参数) 参数
 无
 #### [#](audio.html#示例) 示例：
 ```
audio.play()

```
### [#](audio.html#audio-pause) audio.pause()
 暂停播放音频
 #### [#](audio.html#参数-2) 参数
 无
 #### [#](audio.html#示例-2) 示例：
 ```
audio.pause()

```
### [#](audio.html#audio-stop) audio.stop()
 停止音频播放，可以通过 play 重新播放音频
 #### [#](audio.html#参数-3) 参数
 无
 #### [#](audio.html#示例-3) 示例：
 ```
audio.stop()

```
### [#](audio.html#audio-getplaystate-object) audio.getPlayState(OBJECT)
 获取当前播放状态数据
 #### [#](audio.html#参数-4) 参数
 | 参数名  | 类型  | 必填  | 说明  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 执行结束后的回调  #### [#](audio.html#success-返回值) success 返回值：
 | 参数值  | 类型  | 说明  | state  | String  | 播放状态,分别为'play'，'pause'，'stop'  | src  | String  | 当前播放的音频媒体 uri，停止时返回空字符串  | currentTime  | Number  | 当前音频的当前进度，单位秒，停止时返回-1  | percent  | Number  | 当前播放进度百分比，范围0-100  | autoplay  | Boolen  | 当前音频是否在自动播放  | loop  | Boolen  | 当前音频是否在循环播放  | volume  | Number  | 当前音频的音量，默认当前系统媒体音量，音量变化范围[0.0,1.0]  | muted  | Boolen  | 当前音频是否在静音播放  | duration  | Number  | 音频的播放时长，单位秒，未知返回 NaN  #### [#](audio.html#示例-4) 示例：
 ```
audio.getPlayState({
  success: function(data) {
    console.log(`handling success: state: ${data.state},src:${data.src},currentTime:${data.currentTime},autoplay:${data.autoplay},loop:${data.loop},volume: ${data.volume},muted:${data.muted},notificationVisible:${data.notificationVisible}`)
  },
  fail: function(data, code) {
    console.log('handling fail, code=' + code)
  }
})

```
## [#](audio.html#属性) 属性
 | 名称  | 参数类型  | 是否可读  | 是否可写  | 必填  | 描述  | src  | String  | 是  | 是  | 是  | 播放的音频媒体 uri  | currentTime  | Number  | 是  | 是  | 否  | 音频的当前进度，单位秒，对值设置可以调整播放进度  | duration  | Number  | 是  | 否  | 否  | 音频的播放时长，单位秒，未知返回 NaN  | autoplay  | Boolean  | 是  | 是  | 否  | 音频是否自动播放，默认 false  | loop  | Boolean  | 是  | 是  | 否  | 音频是否循环播放，默认 false  | volume  | Number  | 是  | 是  | 否  | 音频的音量，默认当前系统媒体音量，音量变化范围[0.0,1.0]  | muted  | Boolean  | 是  | 是  | 否  | 音频是否静音，默认 false  | streamType  | String  | 是  | 否  | 否  | 使用音频的类型，可能的值有 music、voicecall，值为 music 时使用扬声器播放，voicecall 时使用听筒播放（手表、手环设备不支持此配置），默认为 music  | meta  | Object  | 否  | 是  | 否  | 音频元数据信息，包括歌名、歌手、专辑名  #### [#](audio.html#示例-5) 示例：
 ```
// let currentTime = audio.currentTime
audio.currentTime = 5

```
## [#](audio.html#事件) 事件
 | 名称  | 描述  | play  | 在调用 play 方法后或者 autoplay 为 true 时的回调事件。被动触发场景举例：1. 蓝牙耳机控制播放音频  | pause  | 在调用 pause 方法后的回调事件。被动触发场景举例：1. 音频焦点被抢占，例如：播放音频时收到来电；2. 蓝牙耳机控制暂停音频  | stop  | 在调用 stop 方法后的回调事件。被动触发场景举例：1. 正在打电话时播放音频  | loadeddata  | 第一次获取到音频数据的回调事件  | ended  | 播放结束时的回调事件  | durationchange  | 播放时长变化时的回调事件  | error  | 播放发生错误时的回调事件  #### [#](audio.html#示例-6) 示例：
 ```
audio.onplay = function() {
  console.log(`audio starts to play`)
}
audio.onplay = null

```


---
## 文件: features\other\index.html

# # 其他

# [#](index.html#其他) 其他
 其他能力。
 
- [音频](audio.html)
 - [弹窗](prompt.html)


---
## 文件: features\other\prompt.html

# # 弹窗 prompt

# [#](prompt.html#弹窗-prompt) 弹窗 prompt
 ## [#](prompt.html#接口声明) 接口声明
 ```
{ "name": "system.prompt" }

```
## [#](prompt.html#导入模块) 导入模块
 ```
import prompt from '@system.prompt' 
// 或 
const prompt = require('@system.prompt')

```
## [#](prompt.html#接口定义) 接口定义
 ### [#](prompt.html#prompt-showtoast-object) prompt.showToast(OBJECT)
 显示 Toast 提示信息
 #### [#](prompt.html#参数) 参数
 | 参数名  | 类型  | 必填  | 说明  | message  | String  | 是  | 显示的文本信息  | duration  | Number  | 否  | 显示持续时间，单位ms，默认值1500，建议区间：1500-10000  #### [#](prompt.html#示例) 示例：
 ```
prompt.showToast({
  message: 'Message Info',
  duration: 2000
})

```


---
## 文件: features\security\crypto.html

# # 密码算法 crypto

# [#](crypto.html#密码算法-crypto) 密码算法 crypto
 ## [#](crypto.html#接口声明) 接口声明
 ```
{ "name": "system.crypto" }

```
## [#](crypto.html#导入模块) 导入模块
 ```
import crypto from '@system.crypto' 
// 或 
const crypto = require('@system.crypto')

```
## [#](crypto.html#接口定义) 接口定义
 ### [#](crypto.html#crypto-hashdigest-object) crypto.hashDigest(OBJECT)
 创建数据的哈希摘要
 #### [#](crypto.html#参数) 参数：
 | 参数名  | 类型  | 必填  | 说明  | data  | String/Uint8Array  | 否  | 待计算内容，和uri二者必须有一个  | uri  | String  | 否  | 待计算文件地址，和data二者必须有一个  | algo  | String  | 否  | 算法 默认： SHA256 
 可选：MD5， SHA1，SHA256，SHA512  #### [#](crypto.html#返回值) 返回值：
 | 类型  | 说明  | String  | 经过计算生成的摘要内容  #### [#](crypto.html#示例) 示例：
 ```
const digest = crypto.hashDigest({
  data: 'hello',
  algo: 'MD5'
})

```
### [#](crypto.html#crypto-hmacdigest-object) crypto.hmacDigest(OBJECT)
 创建加密 HMAC 摘要
 #### [#](crypto.html#参数-2) 参数：
 | 参数名  | 类型  | 必填  | 说明  | data  | String  | 是  | 待计算数据  | algo  | String  | 否  | 算法 默认： SHA256 
 可选：MD5， SHA1，SHA256，SHA512  | key  | String  | 是  | 密钥  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 完成回调  #### [#](crypto.html#success-返回值-object) success 返回值 Object：
 | 参数值  | 类型  | 说明  | data  | String  | 摘要  #### [#](crypto.html#示例-2) 示例：
 ```
crypto.hmacDigest({
  data: 'hello',
  algo: 'SHA256',
  key: 'a secret',
  success: function(res) {
    console.log(`### crypto.hmacDigest success:`, res.data)
  },
  fail: function(data, code) {
    console.log(`### crypto.hmacDigest fail ### ${code}: ${data}`)
  }
})

```
### [#](crypto.html#crypto-sign-object) crypto.sign(OBJECT)
 用于生成签名
 #### [#](crypto.html#参数-3) 参数：
 | 参数名  | 类型  | 必填  | 说明  | data  | String/Uint8Array  | 否  | 被签名文本，和uri二者必须有一个  | uri  | String  | 否  | 被签名文件地址，和data二者必须有一个  | algo  | String  | 否  | 签名算法，默认：'RSA-SHA256' 
 可选：RSA-MD5， RSA-SHA1，RSA-SHA256，RSA-SHA512  | privateKey  | String  | 是  | 私钥  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 完成回调  #### [#](crypto.html#success-返回值-object-2) success 返回值 Object：
 | 参数值  | 类型  | 说明  | data  | String/Uint8Array  | 如果输入为字符串，则返回经过base64编码的字符串；否则返回Uint8Array；如果只传uri，默认返回string  #### [#](crypto.html#示例-3) 示例：
 ```
crypto.sign({
  data: 'hello',
  algo: 'RSA-SHA256',
  privateKey: 'a secret',
  success: function(res) {
    console.log(`### crypto.sign success:`, res.data)
  },
  fail: function(data, code) {
    console.log(`### crypto.sign fail ### ${code}: ${data}`)
  }
})

```
### [#](crypto.html#crypto-verify-object) crypto.verify(OBJECT)
 用于验证签名
 #### [#](crypto.html#参数-4) 参数：
 | 参数名  | 类型  | 必填  | 说明  | data  | String/Uint8Array  | 否  | 被签名文本，和uri二者必须有一个  | uri  | String  | 否  | 被签名文件地址，和data二者必须有一个  | algo  | String  | 否  | 签名算法，默认：'RSA-SHA256' 
 可选：RSA-MD5， RSA-SHA1，RSA-SHA256，RSA-SHA512  | signature  | String/Uint8Array  | 是  | 签名  | publicKey  | String  | 是  | 公钥  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 完成回调  #### [#](crypto.html#success-返回值-boolean) success 返回值 Boolean：
 | 类型  | 说明  | Boolean  | 校验结果，通过为true，不通过为false  #### [#](crypto.html#示例-4) 示例：
 ```
crypto.verify({
  data: 'hello',
  algo: 'RSA-SHA256',
  publicKey: 'public key',
  signature: 'signature',
  success: function(data) {
    console.log(`### crypto.verify success:`, data)
  },
  fail: function(data, code) {
    console.log(`### crypto.verify fail ### ${code}: ${data}`)
  }
})

```
### [#](crypto.html#crypto-encrypt-object) crypto.encrypt(OBJECT)
 加密
 #### [#](crypto.html#参数-5) 参数：
 | 参数名  | 类型  | 必填  | 说明  | data  | String/Uint8Array  | 是  | 待加密数据  | algo  | String  | 否  | 加密算法 默认： RSA 
 可选：RSA， AES  | key  | String  | 是  | 加密使用到的密钥，经过 base64 编码后生成的字符串  | options  | Object  | 否  | 加密参数  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 完成回调  #### [#](crypto.html#rsa-参数options) RSA 参数options：
 | 参数名  | 类型  | 必填  | 说明  | transformation  | String  | 否  | RSA 算法的加密模式和填充项，默认为"RSA/None/PKCS1Padding"  #### [#](crypto.html#aes-参数options) AES 参数options：
 | 参数名  | 类型  | 必填  | 说明  | transformation  | String  | 否  | AES 算法的加密模式和填充项，默认为"AES/CBC/PKCS7Padding"  | iv  | String  | 否  | AES 加解密的初始向量，经过 base64 编码后的字符串，默认值为 key 值  | ivOffset  | Number  | 否  | AES 加解密的初始向量偏移，整数，默认值为 0  | ivLen  | Number  | 否  | AES 加解密的初始向量字节长度，整数，默认值为 16  #### [#](crypto.html#success-返回值-object-3) success 返回值 Object：
 | 参数值  | 类型  | 说明  | data  | String/Uint8Array  | 如果输入为字符串，则返回经过base64编码的字符串；否则返回Uint8Array  #### [#](crypto.html#示例-5) 示例：
 ```
crypto.encrypt({
  //待加密的文本内容
  data: 'hello',
  //base64编码后的加密公钥
  key: crypto.btoa('KEYKEYKEYKEYKEYK'),
  algo: 'AES',
  success: function(res) {
    console.log(`### crypto.encrypt success:`, res.data)
  },
  fail: function(data, code) {
    console.log(`### crypto.encrypt fail ### ${code}: ${data}`)
  }
})

```
### [#](crypto.html#crypto-decrypt-object) crypto.decrypt(OBJECT)
 解密
 #### [#](crypto.html#参数-6) 参数：
 | 参数名  | 类型  | 必填  | 说明  | data  | String/Uint8Array  | 是  | 待解密数据  | algo  | String  | 否  | 解密算法 默认： RSA 
 可选：RSA， AES  | key  | String  | 是  | 加密或解密使用到的密钥，经过 base64 编码后生成的字符串  | options  | Object  | 否  | 解密参数  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 完成回调  #### [#](crypto.html#rsa-参数options-2) RSA 参数options：
 | 参数名  | 类型  | 必填  | 说明  | transformation  | String  | 否  | RSA 算法的加密模式和填充项，默认为"RSA/None/PKCS1Padding"  #### [#](crypto.html#aes-参数options-2) AES 参数options：
 | 参数名  | 类型  | 必填  | 说明  | transformation  | String  | 否  | AES 算法的加密模式和填充项，默认为"AES/CBC/PKCS7Padding"  | iv  | String  | 否  | AES 加解密的初始向量，经过 base64 编码后的字符串，默认值为 key 值  | ivOffset  | Number  | 否  | AES 加解密的初始向量偏移，整数，默认值为 0  | ivLen  | Number  | 否  | AES 加解密的初始向量字节长度，整数，默认值为 16  #### [#](crypto.html#success-返回值-object-4) success 返回值 Object：
 | 参数值  | 类型  | 说明  | data  | String/Uint8Array  | 如果输入为字符串，则返回经过base64编码的字符串；否则返回Uint8Array  #### [#](crypto.html#示例-6) 示例：
 ```
crypto.decrypt({
  //待解密的内容
  data: 'WB96uM08PfYIHu5G1p6YwA==',
  //base64编码后的加密公钥
  key: crypto.btoa('KEYKEYKEYKEYKEYK'),
  algo: 'AES',
  success: function(res) {
    console.log(`### crypto.decrypt success:`, res.data)
  },
  fail: function(data, code) {
    console.log(`### crypto.decrypt fail ### ${code}: ${data}`)
  }
})

```
### [#](crypto.html#crypto-btoa-string) crypto.btoa(STRING)
 从String对象中创建一个 base-64 编码的 ASCII 字符串，其中字符串中的每个字符都被视为一个二进制数据字节
 #### [#](crypto.html#参数-7) 参数：
 | 类型  | 必填  | 说明  | String  | 是  | 待编码文本  #### [#](crypto.html#返回值-string) 返回值 String：
 | 类型  | 说明  | String  | 经过编码之后的结果  #### [#](crypto.html#示例-7) 示例：
 ```
const encodeData = crypto.btoa('hello')

```
### [#](crypto.html#crypto-atob-string) crypto.atob(STRING)
 对经过 base-64 编码的字符串进行解码
 #### [#](crypto.html#参数-8) 参数：
 | 类型  | 必填  | 说明  | String  | 是  | 待解码文本  #### [#](crypto.html#返回值-string-2) 返回值 String：
 | 类型  | 说明  | String  | 经过解码之后的结果  #### [#](crypto.html#示例-8) 示例：
 ```
const encodeString = crypto.btoa('hello')
const res = crypto.atob(encodeString)

```
## [#](crypto.html#支持明细) 支持明细
 | 设备产品  | 说明  | 小米 S1 Pro 运动健康手表  | 不支持  | 小米手环 8 Pro  | 不支持  | 小米手环 9 / 9 Pro  | 不支持  | Xiaomi Watch S3  | 支持  | Redmi Watch 4  | 不支持  | 小米腕部心电血压记录仪  | 不支持  | 小米手环 10  | 不支持  | Xiaomi Watch S4  | 支持  | REDMI Watch 5  | 支持  | REDMI Watch 6  | 支持


---
## 文件: features\security\index.html

# # 安全

# [#](index.html#安全) 安全
 提供安全、加密相关能力。
 
- [密码算法](crypto.html)


---
## 文件: features\system\battery.html

# # 电量信息 battery

# [#](battery.html#电量信息-battery) 电量信息 battery
 ## [#](battery.html#接口声明) 接口声明
 ```
{ "name": "system.battery" }

```
## [#](battery.html#导入模块) 导入模块
 ```
import battery from '@system.battery' 
// 或 
const battery = require('@system.battery')

```
## [#](battery.html#接口定义) 接口定义
 ### [#](battery.html#battery-getstatus-object) battery.getStatus(OBJECT)
 获取当前设备的电量信息
 #### [#](battery.html#参数) 参数：
 | 参数名  | 类型  | 必填  | 说明  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 执行结束后的回调  #### [#](battery.html#success-返回值) success 返回值：
 | 参数名  | 类型  | 说明  | charging  | Boolean  | 是否正在充电  | level  | Number  | 当前电量，0.0 - 1.0 之间  #### [#](battery.html#示例) 示例
 ```
battery.getStatus({
  success: function(data) {
      console.log(`handling success: ${data.level}`)
  },
  fail: function(data, code) {
      console.log(`handling fail, code = ${code}`)
  }
})

```
## [#](battery.html#支持明细) 支持明细
 | 设备产品  | 说明  | 小米 S1 Pro 运动健康手表  | 不支持  | 小米手环 8 Pro  | 不支持  | 小米手环 9 / 9 Pro  | 不支持  | Xiaomi Watch S3  | 不支持  | Redmi Watch 4  | 不支持  | 小米腕部心电血压记录仪  | 不支持  | 小米手环 10  | 支持  | Xiaomi Watch S4  | 支持  | REDMI Watch 5  | 支持  | REDMI Watch 6  | 支持


---
## 文件: features\system\brightness.html

# # 屏幕亮度 brightness

# [#](brightness.html#屏幕亮度-brightness) 屏幕亮度 brightness
 ## [#](brightness.html#接口声明) 接口声明
 ```
{ "name": "system.brightness" }

```
## [#](brightness.html#导入模块) 导入模块
 ```
import brightness from '@system.brightness' 
// 或 
const brightness = require('@system.brightness')

```
## [#](brightness.html#接口定义) 接口定义
 ### [#](brightness.html#brightness-getvalue-object) brightness.getValue(OBJECT)
 获得当前屏幕亮度值
 #### [#](brightness.html#参数) 参数：
 | 参数名  | 类型  | 必填  | 说明  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 执行结束后的回调  #### [#](brightness.html#success-返回值) success 返回值：
 | 参数值  | 类型  | 说明  | value  | Integer  | 屏幕亮度，取值范围 0-255  #### [#](brightness.html#示例) 示例：
 ```
brightness.getValue({
  success: function(data) {
    console.log(`handling success, value = ${data.value}`)
  },
  fail: function(data, code) {
    console.log(`handling fail, code = ${code}`)
  }
})

```
### [#](brightness.html#brightness-setvalue-object) brightness.setValue(OBJECT)
 设置当前屏幕亮度值
 #### [#](brightness.html#参数-2) 参数：
 | 参数名  | 类型  | 必填  | 说明  | value  | Integer  | 是  | 屏幕亮度，取值范围 0-255  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 执行结束后的回调  #### [#](brightness.html#示例-2) 示例：
 ```
brightness.setValue({
  value: 100,
  success: function() {
    console.log('handling success')
  },
  fail: function(data, code) {
    console.log(`handling fail, code = ${code}`)
  }
})

```
### [#](brightness.html#brightness-getmode-object) brightness.getMode(OBJECT)
 获得当前屏幕亮度模式
 #### [#](brightness.html#参数-3) 参数：
 | 参数名  | 类型  | 必填  | 说明  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 执行结束后的回调  #### [#](brightness.html#success-返回值-2) success 返回值：
 | 参数值  | 类型  | 说明  | mode  | Integer  | 0 为手动调节屏幕亮度，1 为自动调节屏幕亮度  #### [#](brightness.html#示例-3) 示例：
 ```
brightness.getMode({
  success: function(data) {
    console.log(`handling success, mode = ${data.mode}`)
  },
  fail: function(data, code) {
    console.log(`handling fail, code = ${code}`)
  }
})

```
### [#](brightness.html#brightness-setmode-object) brightness.setMode(OBJECT)
 设置当前屏幕亮度模式
 #### [#](brightness.html#参数-4) 参数：
 | 参数名  | 类型  | 必填  | 说明  | mode  | Integer  | 是  | 0 为手动调节屏幕亮度，1 为自动调节屏幕亮度  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 执行结束后的回调  #### [#](brightness.html#示例-4) 示例：
 ```
brightness.setMode({
  mode: 1,
  success: function() {
    console.log('handling success')
  },
  fail: function(data, code) {
    console.log(`handling fail, code = ${code}`)
  }
})

```
### [#](brightness.html#brightness-setkeepscreenon-object) brightness.setKeepScreenOn(OBJECT)
 设置是否保持常亮状态
 #### [#](brightness.html#参数-5) 参数：
 | 参数名  | 类型  | 必填  | 说明  | keepScreenOn  | Boolean  | 是  | 是否保持屏幕常亮  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 执行结束后的回调  #### [#](brightness.html#示例-5) 示例：
 ```
brightness.setKeepScreenOn({
  keepScreenOn: true,
  success: function() {
    console.log('handling success')
  },
  fail: function(data, code) {
    console.log(`handling fail, code = ${code}`)
  }
})

```


---
## 文件: features\system\event.html

# # 事件 event4+

# [#](event.html#事件-event) 事件 event[4+](../../guide/version/APILevel4.html)
 ## [#](event.html#接口声明) 接口声明
 ```
{ "name": "system.event" }

```
## [#](event.html#导入模块) 导入模块
 ```
import event from '@system.event' 
// 或 
const event = require('@system.event')

```
## [#](event.html#接口定义) 接口定义
 ### [#](event.html#event-publish-object) event.publish (OBJECT)
 发布公共事件
 #### [#](event.html#参数) 参数：
 | 参数名  | 类型  | 必填  | 说明  | eventName  | String  | 是  | 事件名称，公共事件保留名称被系统占用，请勿使用  | options  | Object  | 否  | 事件参数  #### [#](event.html#options-参数) options 参数：
 | 参数名  | 类型  | 必填  | 说明  | params  | Object  | 否  | 事件参数  | permissions  | Array  | 否  | 订阅者的权限，拥有权限的包才能收到发送的事件  #### [#](event.html#系统支持的公共事件) 系统支持的公共事件：
 | 系统内部事件名称  | 订阅者所需权限  | 说明  | usual.event.BATTERY_CHANGED  | 无  | 电量改变，参数：level:0.0 - 1.0 之间  | usual.event.DISCHARGING  | 无  | 停止充电  | usual.event.CHARGING  | 无  | 开始充电  #### [#](event.html#返回值) 返回值：
 无
 #### [#](event.html#示例) 示例：
 ```
event.publish({
  eventName: 'myEventName',
  options: {
    params: { age: 10, name: 'peter' },
    permissions: ['com.example.demo']
  }
})

```
### [#](event.html#event-subscribe-object) event.subscribe(OBJECT)
 订阅公共事件
 #### [#](event.html#参数-2) 参数：
 | 参数名  | 类型  | 必填  | 说明  | eventName  | String  | 是  | 事件名称  | callback  | Function  | 是  | 回调函数  #### [#](event.html#回调参数) 回调参数：
 | 参数名  | 类型  | 必填  | 说明  | params  | Object  | 否  | 事件参数  | package  | String  | 否  | 事件推送者包名  #### [#](event.html#返回值-2) 返回值：
 | 类型  | 必填  | 说明  | Number  | 是  | 事件id，订阅失败返回undefined  #### [#](event.html#示例-2) 示例：
 ```
const evtId = event.subscribe({
  eventName: 'myEventName',
  callback: function(res) {
    if (res.package === 'com.example.demo') {
      console.log(res.params)
    }
  }
})
console.log(evtId)

```
### [#](event.html#event-unsubscribe-object) event.unsubscribe(OBJECT)
 取消订阅公共事件
 #### [#](event.html#参数-3) 参数：
 | 参数名  | 类型  | 必填  | 说明  | id  | Number  | 是  | 订阅id  #### [#](event.html#示例-3) 示例：
 ```
const evtId = event.subscribe({
  eventName: 'myEventName',
  callback: function(res) {
    if (res.package === 'com.example.demo') {
      console.log(res.params)
    }
  }
})

event.unsubscribe({ id: evtId })

```
## [#](event.html#支持明细) 支持明细
 | 设备产品  | 说明  | 小米 S1 Pro 运动健康手表  | 不支持  | 小米手环 8 Pro  | 不支持  | 小米手环 9 / 9 Pro  | 不支持  | Xiaomi Watch S3  | 不支持  | Redmi Watch 4  | 不支持  | 小米腕部心电血压记录仪  | 不支持  | 小米手环 10  | 支持  | Xiaomi Watch S4  | 支持  | REDMI Watch 5  | 支持  | REDMI Watch 6  | 支持


---
## 文件: features\system\geolocation.html

# # 地理位置 geolocation

# [#](geolocation.html#地理位置-geolocation) 地理位置 geolocation
 ## [#](geolocation.html#接口声明) 接口声明
 ```
{ "name": "system.geolocation" }

```
## [#](geolocation.html#导入模块) 导入模块
 ```
import geolocation from '@system.geolocation' 
// 或 
const geolocation = require('@system.geolocation')

```
## [#](geolocation.html#接口定义) 接口定义
 ### [#](geolocation.html#geolocation-getlocation-object) geolocation.getLocation(OBJECT)
 获取地理位置
 #### [#](geolocation.html#权限要求) 权限要求
 精确设备定位
 开发者需要在 manifest.json 里面配置权限：
 ```
{
  "permissions": [
    { "name": "hapjs.permission.LOCATION" }
  ]
}

```
#### [#](geolocation.html#参数) 参数：
 | 参数名  | 类型  | 必填  | 说明  | timeout  | Number  | 否  | 设置超时时间，单位是 ms，默认值为 30000  | success  | Function  | 是  | 成功回调  | fail  | Function  | 否  | 失败回调，可能是因为缺乏权限  | complete  | Function  | 否  | 执行结束后的回调  #### [#](geolocation.html#success-返回值) success 返回值：
 | 参数名  | 类型  | 说明  | longitude  | Number  | 经度，浮点数  | latitude  | Number  | 纬度，浮点数  | altitude  | Number  | 海拔、高度，单位m，浮点数  | speed  | Number  | 速度值，单位m/s，浮点数  | accuracy  | Number  | 精确度，值为正整数  | accuracyInfo  | { horizontal: Number, vertical: Number }  | 精确度信息，包含水平和垂直方向精准度  #### [#](geolocation.html#fail-返回错误代码) fail 返回错误代码：
 | 错误码  | 说明  | 203  | 该功能不支持  | 204  | 超时返回  #### [#](geolocation.html#示例) 示例：
 ```
geolocation.getLocation({
  success: function(data) {
    console.log(
      `handling success: longitude = ${data.longitude}, latitude = ${
        data.latitude
      }, speed = ${data.speed}, altitude = ${data.altitude}`
    )
  },
  fail: function(data, code) {
    console.log(`handling fail, code = ${code}, errorMsg=${data}`)
  }
})

```
### [#](geolocation.html#geolocation-subscribe-object) geolocation.subscribe(OBJECT)
 监听地理位置。如果多次调用，仅最后一次调用生效
 #### [#](geolocation.html#权限要求-2) 权限要求
 精确设备定位
 开发者需要在 manifest.json 里面配置权限：
 ```
{
  "permissions": [
    { "name": "hapjs.permission.LOCATION" }
  ]
}

```
#### [#](geolocation.html#参数-2) 参数：
 | 参数名  | 类型  | 必填  | 说明  | callback  | Function  | 是  | 每次位置信息发生变化，都会被回调  | fail  | Function  | 否  | 失败回调  #### [#](geolocation.html#callback-返回值) callback 返回值：
 | 参数名  | 类型  | 说明  | longitude  | Number  | 经度，浮点数  | latitude  | Number  | 纬度，浮点数  | altitude  | Number  | 海拔、高度，单位m，浮点数  | speed  | Number  | 速度值，单位m/s，浮点数  | accuracy  | Number  | 精确度，值为正整数  #### [#](geolocation.html#fail-返回错误代码-2) fail 返回错误代码：
 | 错误码  | 说明  | 203  | 该功能不支持  #### [#](geolocation.html#示例-2) 示例：
 ```
geolocation.subscribe({
  callback: function(data) {
    console.log(
      `handling success: longitude = ${data.longitude}, latitude = ${
        data.latitude
      }, speed = ${data.speed}, altitude = ${data.altitude}`
    )
  },
  fail: function(data, code) {
    console.log(`handling fail, code = ${code}, errorMsg=${data}`)
  }
})

```
### [#](geolocation.html#geolocation-unsubscribe) geolocation.unsubscribe()
 取消监听地理位置
 #### [#](geolocation.html#权限要求-3) 权限要求
 精确设备定位
 开发者需要在 manifest.json 里面配置权限：
 ```
{
  "permissions": [
    { "name": "hapjs.permission.LOCATION" }
  ]
}

```
#### [#](geolocation.html#参数-3) 参数：
 无
 #### [#](geolocation.html#示例-3) 示例：
 ```
geolocation.unsubscribe()

```
## [#](geolocation.html#支持明细) 支持明细
 | 设备产品  | 说明  | 小米 S1 Pro 运动健康手表  | 不支持  | 小米手环 8 Pro  | 不支持  | 小米手环 9 / 9 Pro  | 不支持  | Xiaomi Watch S3  | 支持  | Redmi Watch 4  | 不支持  | 小米腕部心电血压记录仪  | 不支持  | 小米手环 10  | 不支持  | Xiaomi Watch S4  | 支持  | REDMI Watch 5  | 支持  | REDMI Watch 6  | 支持


---
## 文件: features\system\index.html

# # 系统能力

# [#](index.html#系统能力) 系统能力
 提供一些系统能力。
 
- [网络信息](network.html)
 - [振动](vibrator.html)
 - [屏幕亮度](brightness.html)
 - [录音](record.html)
 - [地理位置](geolocation.html)
 - [传感器](sensor.html)
 - [事件](event.html)
 - [电量信息](battery.html)
 - [系统音量](volume.html)
 - [解压缩](zip.html)


---
## 文件: features\system\network.html

# # 网络信息 network

# [#](network.html#网络信息-network) 网络信息 network
 ## [#](network.html#接口声明) 接口声明
 ```
{ "name": "system.network" }

```
## [#](network.html#导入模块) 导入模块
 ```
import network from '@system.network' 
// 或 
const network = require('@system.network')

```
## [#](network.html#接口定义) 接口定义
 ### [#](network.html#network-gettype-object) network.getType(OBJECT)
 获取网络类型
 #### [#](network.html#参数) 参数：
 | 参数名  | 类型  | 必填  | 说明  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调，可能是因为缺乏权限  | complete  | Function  | 否  | 执行结束后的回调  #### [#](network.html#success-返回object值) success 返回Object值：
 | 参数名  | 类型  | 说明  | type  | String  | 网络类型，可能的值为 2g，3g，4g，wifi，none，5g，bluetooth，others  #### [#](network.html#示例) 示例：
 ```
network.getType({
  success: function(data) {
    console.log(`handling success: ${data.type}`)
  }
})

```
### [#](network.html#network-subscribe-object) network.subscribe(OBJECT)
 监听网络类型变化。如果多次调用，仅最后一次调用生效
 #### [#](network.html#参数-2) 参数：
 | 参数名  | 类型  | 必填  | 说明  | callback  | Function  | 否  | 每次网络发生变化，都会被回调  | fail  | Function  | 否  | 失败回调，可能是因为缺乏权限  #### [#](network.html#callback-返回object值) callback 返回Object值：
 | 参数名  | 类型  | 说明  | type  | String  | 网络类型，可能的值为 2g，3g，4g，wifi，none，5g，bluetooth，others。注：网络类型为 none 以外的值并不保证设备一定能访问到目标服务器，需要请求接口进行判断  #### [#](network.html#示例-2) 示例：
 ```
network.subscribe({
  callback: function(data) {
    console.log(`handling callback ${data.type}`)
  }
})

```
### [#](network.html#network-unsubscribe) network.unsubscribe()
 取消监听网络类型变化
 #### [#](network.html#参数-3) 参数：
 无
 #### [#](network.html#示例-3) 示例：
 ```
network.unsubscribe()

```
## [#](network.html#支持明细) 支持明细
 | 设备产品  | 说明  | 小米 S1 Pro 运动健康手表  | 支持  | 小米手环 8 Pro  | 不支持  | 小米手环 9 / 9 Pro  | 不支持  | Xiaomi Watch S3  | 支持  | Redmi Watch 4  | 不支持  | 小米腕部心电血压记录仪  | 不支持  | 小米手环 10  | 不支持  | Xiaomi Watch S4  | 支持  | REDMI Watch 5  | 支持  | REDMI Watch 6  | 支持


---
## 文件: features\system\record.html

# # 录音 record

# [#](record.html#录音-record) 录音 record
 ## [#](record.html#接口声明) 接口声明
 ```
{ "name": "system.record" }

```
## [#](record.html#导入模块) 导入模块
 ```
import record from '@system.record' 
// 或 
const record = require('@system.record')

```
## [#](record.html#接口定义) 接口定义
 ### [#](record.html#record-start-object) record.start(OBJECT)
 开始录音
 #### [#](record.html#参数) 参数：
 | 参数名  | 类型  | 必填  | 说明  | duration  | Number  | 否  | 录音时长，单位为 ms。如果 duration 为有效值将在达到指定值时停止录音  | sampleRate  | Number  | 否  | 采样率。不同的音频格式所支持的采样率范围不同。默认设置为 8000，建议使用 8000/16000/32000/44100/48000  | numberOfChannels  | Number  | 否  | 录音通道数，有效值 1/2  | encodeBitRate  | Number  | 否  | 编码码率。编码码率的取值与采样率和音频格式有关  | format  | String  | 否  | 音频格式，有效值 pcm/opus/wav。缺省为 pcm  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 执行结束后的回调  #### [#](record.html#success-返回值) success 返回值：
 | 参数名  | 类型  | 说明  | uri  | String  | 录音文件的存储路径，在应用的缓存目录中  #### [#](record.html#fail-返回错误码) fail 返回错误码：
 | 错误码  | 说明  | 205  | 录音已在进行中  | 202  | 参数错误  #### [#](record.html#示例) 示例：
 ```
record.start({
  duration: 10000,
  sampleRate: 8000,
  numberOfChannels: 1,
  encodeBitRate: 128000,
  format: 'pcm',
  success: function(data) {
    console.log(`handling success: ${data.uri}`)
  },
  fail: function(data, code) {
    console.log(`handling fail, code = ${code}, errorMsg=${data}`)
  },
  complete: function () {
    console.log(`handling complete`)
  }
})

```
### [#](record.html#record-stop) record.stop()
 停止录音
 #### [#](record.html#参数-2) 参数：
 无
 #### [#](record.html#示例-2) 示例：
 ```
record.stop()

```
## [#](record.html#支持明细) 支持明细
 | 设备产品  | 说明  | 小米 S1 Pro 运动健康手表  | 不支持  | 小米手环 8 Pro  | 不支持  | 小米手环 9 / 9 Pro  | 不支持  | Xiaomi Watch S3  | 不支持  | Redmi Watch 4  | 不支持  | 小米腕部心电血压记录仪  | 不支持  | 小米手环 10  | 不支持  | Xiaomi Watch S4  | 不支持  | REDMI Watch 5  | 不支持  | REDMI Watch 6  | 不支持


---
## 文件: features\system\sensor.html

# # 传感器 sensor

# [#](sensor.html#传感器-sensor) 传感器 sensor
 ## [#](sensor.html#接口声明) 接口声明
 ```
{ "name": "system.sensor" }

```
## [#](sensor.html#导入模块) 导入模块
 ```
import sensor from '@system.sensor' 
// 或 
const sensor = require('@system.sensor')

```
## [#](sensor.html#接口定义) 接口定义
 ### [#](sensor.html#方法) 方法
 ### [#](sensor.html#sensor-subscribepressure-object) sensor.subscribePressure(OBJECT)
 监听压力、压强感应数据。如果多次调用，仅最后一次调用生效
 #### [#](sensor.html#参数) 参数：
 | 参数名  | 类型  | 必填  | 说明  | callback  | Function  | 是  | 每次位置信息发生变化，都会被回调  #### [#](sensor.html#callback-返回值) callback 返回值：
 | 参数名  | 类型  | 说明  | pressure  | Number  | 压力、压强，单位hpa，百帕，浮点数  #### [#](sensor.html#示例) 示例：
 ```
sensor.subscribePressure({
  callback: function(ret) {
    console.log(`handling callback, pressure = ${ret.pressure}`)
  }
})

```
### [#](sensor.html#sensor-unsubscribepressure) sensor.unsubscribePressure()
 取消压力、压强感应数据
 #### [#](sensor.html#参数-2) 参数：
 无
 #### [#](sensor.html#示例-2) 示例：
 ```
sensor.unsubscribePressure()

```
### [#](sensor.html#sensor-subscribeaccelerometer-object) sensor.subscribeAccelerometer(OBJECT)
 监听加速度感应数据
 #### [#](sensor.html#参数-3) 参数：
 | 参数名  | 类型  | 必填  | 说明  | interval  | String  | 否  | 监听加速度数据回调函数的执行频率，默认normal  | callback  | Function  | 是  | 重力感应数据变化后会回调此函数  | fail  | Function  | 否  | 订阅错误回调  #### [#](sensor.html#interval-的合法值) interval 的合法值：
 | 值  | 说明  | game  | 适用于更新游戏的回调频率，在 20ms/次 左右  | ui  | 适用于更新 UI 的回调频率，在 60ms/次 左右  | normal  | 普通的回调频率，在 200ms/次 左右  #### [#](sensor.html#callback-返回值-2) callback 返回值：
 | 参数名  | 类型  | 说明  | x  | Number  | x 轴坐标  | y  | Number  | y 轴坐标  | z  | Number  | z 轴坐标  #### [#](sensor.html#示例-3) 示例：
 ```
sensor.subscribeAccelerometer({
  callback: function(ret) {
    console.log(`handling callback, x = ${ret.x}, y = ${ret.y}, z = ${ret.z}`)
  },
  fail: function(msg, code) {
    console.log(`handling callback, fail:`, msg, code)
  }
})

```
### [#](sensor.html#sensor-unsubscribeaccelerometer) sensor.unsubscribeAccelerometer()
 取消监听加速度感应数据
 #### [#](sensor.html#参数-4) 参数：
 无
 #### [#](sensor.html#示例-4) 示例：
 ```
sensor.unsubscribeAccelerometer()

```
### [#](sensor.html#sensor-subscribecompass-object) sensor.subscribeCompass(OBJECT)
 监听罗盘数据。如果多次调用，仅最后一次调用生效
 #### [#](sensor.html#参数-5) 参数：
 | 参数名  | 类型  | 必填  | 说明  | callback  | Function  | 是  | 罗盘数据变化后会回调此函数  | fail  | Function  | 否  | 订阅失败回调  #### [#](sensor.html#callback-返回值-3) callback 返回值：
 | 参数名  | 类型  | 说明  | direction  | Number  | 表示设备的 y 轴和地球磁场北极之间的角度，当面朝北，角度为 0；朝南角度为 π；朝东角度 π/2；朝西角度-π/2  | accuracy  | Number  | 精度，详见compass精度说明  #### [#](sensor.html#fail-返回错误代码) fail 返回错误代码：
 | 错误码  | 说明  | 1000  | 当前设备不支持罗盘传感器  #### [#](sensor.html#示例-5) 示例：
 ```
sensor.subscribeCompass({
  callback: function (res) {
    console.log(`handling subscribeCompass callback, direction = ${res.direction}, accuracy = ${res.accuracy}`)
  },
  fail: function (data, code) {
    console.log(`handling subscribeCompass fail, code = ${code}`)
  }
})

```
### [#](sensor.html#sensor-unsubscribecompass) sensor.unsubscribeCompass()
 取消监听加速度感应数据
 #### [#](sensor.html#参数-6) 参数：
 无
 #### [#](sensor.html#示例-6) 示例：
 ```
sensor.unsubscribeCompass()

```
 ### [#](sensor.html#compass精度说明) compass精度说明：
 | 值  | 说明  | 3  | 高精度  | 2  | 中等精度  | 1  | 低精度  | -1  | 不可信，传感器失去连接  | 0  | 不可信，原因未知  ## [#](sensor.html#支持明细) 支持明细
 | 接口  | 已支持设备产品  | 不支持设备产品  | subscribePressure  | Xiaomi Watch S3、小米手环 9 Pro、小米手环 10、Xiaomi Watch S4  | 小米 S1 Pro 运动健康手表、小米手环 8 Pro、小米手环 9、Redmi Watch 4、Xiaomi Watch H1、REDMI Watch 5、REDMI Watch 6  | unsubscribePressure  | Xiaomi Watch S3、小米手环 9 Pro、小米手环 10、Xiaomi Watch S4  | 小米 S1 Pro 运动健康手表、小米手环 8 Pro、小米手环 9、Redmi Watch 4、Xiaomi Watch H1、REDMI Watch 5、REDMI Watch 6  | subscribeAccelerometer  | 小米手环 9 / 9 Pro、小米手环 10  | Xiaomi Watch S3、小米 S1 Pro 运动健康手表、小米手环 8 Pro、Redmi Watch 4、Xiaomi Watch H1、Xiaomi Watch S4、REDMI Watch 5、REDMI Watch 6  | unsubscribeAccelerometer  | 小米手环 9 / 9 Pro、小米手环 10  | Xiaomi Watch S3、小米 S1 Pro 运动健康手表、小米手环 8 Pro、Redmi Watch 4、Xiaomi Watch H1、Xiaomi Watch S4、REDMI Watch 5、REDMI Watch 6  | subscribeCompass / unsubscribeCompass  | Xiaomi Watch S4、REDMI Watch 5 、REDMI Watch 6  | 其余小米环表设备


---
## 文件: features\system\vibrator.html

# # 振动 vibrator

# [#](vibrator.html#振动-vibrator) 振动 vibrator
 ## [#](vibrator.html#接口声明) 接口声明
 ```
{ "name": "system.vibrator" }

```
## [#](vibrator.html#导入模块) 导入模块
 ```
import vibrator from '@system.vibrator' 
// 或 
const vibrator = require('@system.vibrator')

```
## [#](vibrator.html#接口定义) 接口定义
 ### [#](vibrator.html#vibrator-vibrate-object) vibrator.vibrate(OBJECT)
 触发振动
 #### [#](vibrator.html#参数) 参数：
 | 参数  | 类型  | 必填  | 说明  | mode  | String  | 否  | 振动模式，"long"表示长振动，"short"表示短振动。默认为 long  #### [#](vibrator.html#示例) 示例：
 ```
vibrator.vibrate({
  mode: 'long'
})

```
### [#](vibrator.html#vibrator-start-object) vibrator.start(OBJECT)
 开始振动
 #### [#](vibrator.html#参数-2) 参数：
 | 参数  | 类型  | 必填  | 说明  | duration  | Number  | 是  | 振动持续时间(单位 ms)，必须为正整数  | interval  | Number  | 是  | 振动间隔时间(单位 ms)，必须为正整数  | count  | Number  | 是  | 振动次数，必须为正整数  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 执行结束后的回调  #### [#](vibrator.html#success-返回值) success 返回值：
 | 参数名  | 类型  | 说明  | id  | Number  | 唯一的 ID，标识振动任务  #### [#](vibrator.html#fail-返回值) fail 返回值：
 | 错误码  | 说明  | 205  | 任务已存在  | 202  | 参数错误  #### [#](vibrator.html#示例-2) 示例：
 ```
vibrator.start({
  duration: 1000,
  interval: 1000,
  count: 10,
  success: function (data) {
    console.log(`handling success, id = ${data.id}`)
  },
  fail: function(data, code) {
    console.log(`handling fail, code = ${code}, errorMsg=${data}`)
  },
  complete: function () {
    console.log(`handling complete`)
  }
})

```
### [#](vibrator.html#vibrator-stop-number) vibrator.stop(Number)
 停止振动
 #### [#](vibrator.html#参数-3) 参数：
 | 类型  | 必填  | 说明  | Number  | 是  | 振动任务 ID  #### [#](vibrator.html#返回值) 返回值：
 | 类型  | 说明  | Boolean  | true：成功；false：失败  #### [#](vibrator.html#示例-3) 示例：
 ```
vibrator.stop(1)

```
### [#](vibrator.html#vibrator-getsystemdefaultmode) vibrator.getSystemDefaultMode()
 获取系统默认振动模式
 #### [#](vibrator.html#参数-4) 参数：
 无
 #### [#](vibrator.html#返回值-2) 返回值：
 | 类型  | 说明  | Number  | 0：关闭振动；1：标准振动；2：加强振动  #### [#](vibrator.html#示例-4) 示例：
 ```
vibrator.getSystemDefaultMode()

```
## [#](vibrator.html#支持明细) 支持明细
 | 接口  | 已支持设备产品  | 不支持设备产品  | vibrate  | 小米 S1 Pro 运动健康手表、小米手环 8 Pro、小米手环 9 / 9 Pro、Redmi Watch 4、Xiaomi Watch H1、Xiaomi Watch S3、小米手环 10、Xiaomi Watch S4、REDMI Watch 5、REDMI Watch 6  | -  | start  | -  | Xiaomi Watch S3、Xiaomi Watch S4、REDMI Watch 5、REDMI Watch 6、小米 S1 Pro 运动健康手表、小米手环 8 Pro、小米手环 9 / 9 Pro、小米手环 10、Redmi Watch 4、Xiaomi Watch H1  | stop  | -  | Xiaomi Watch S3、Xiaomi Watch S4、REDMI Watch 5、REDMI Watch 6、小米 S1 Pro 运动健康手表、小米手环 8 Pro、小米手环 9 / 9 Pro、小米手环 10、Redmi Watch 4、Xiaomi Watch H1  | getSystemDefaultMode  | -  | Xiaomi Watch S3、Xiaomi Watch S4、REDMI Watch 5、REDMI Watch 6、小米 S1 Pro 运动健康手表、小米手环 8 Pro、小米手环 9 / 9 Pro、小米手环 10、Redmi Watch 4、Xiaomi Watch H1


---
## 文件: features\system\volume.html

# # 系统音量 volume

# [#](volume.html#系统音量-volume) 系统音量 volume
 ## [#](volume.html#接口声明) 接口声明
 ```
{ "name": "system.volume" }

```
## [#](volume.html#导入模块) 导入模块
 ```
import volume from '@system.volume' 
// 或 
const volume = require('@system.volume')

```
## [#](volume.html#接口定义) 接口定义
 ### [#](volume.html#volume-getmediavalue-object) volume.getMediaValue (OBJECT)
 获取当前多媒体音量
 #### [#](volume.html#参数) 参数
 | 参数名  | 类型  | 必填  | 说明  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 执行结束后的回调  #### [#](volume.html#success-返回值) success 返回值：
 | 参数值  | 类型  | 说明  | value  | Number  | 系统媒体当前音量，0.0-1.0 之间  #### [#](volume.html#示例) 示例
 ```
volume.getMediaValue({
  success: function(data) {
    console.log(`handling success: ${data.value}`)
  },
  fail: function(data, code) {
    console.log(`handling fail, code = ${code}`)
  }
})

```
### [#](volume.html#volume-setmediavalue-object) volume.setMediaValue (OBJECT)
 设置当前多媒体音量
 #### [#](volume.html#参数-2) 参数
 | 参数名  | 类型  | 必填  | 说明  | value  | Number  | 是  | 设置的音量，0.0-1.0 之间  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 执行结束后的回调  #### [#](volume.html#fail-返回值) fail 返回值：
 [支持通用错误码](../grammar.html#通用错误码)
 #### [#](volume.html#示例-2) 示例
 ```
volume.setMediaValue({
  value: 0.5,
  success: function() {
    console.log('handling success')
  },
  fail: function(data, code) {
    console.log(`handling fail, code = ${code}`)
  }
})

```
## [#](volume.html#事件) 事件
 ### [#](volume.html#volume-onmediavaluechanged) volume.onMediaValueChanged
 多媒体音量发生变化事触发
 #### [#](volume.html#回调object参数) 回调Object参数：
 | 参数名  | 类型  | 必填  | 说明  | value  | Number  | 是  | 系统媒体当前音量，范围：0.0-1.0 之间  #### [#](volume.html#示例-3) 示例
 ```
volume.onMediaValueChanged = function(res) {
  console.log('volume media value changed:', res.value)
}

```


---
## 文件: features\system\zip.html

# # 解压缩 zip

# [#](zip.html#解压缩-zip) 解压缩 zip
 ## [#](zip.html#接口声明) 接口声明
 ```
{ "name": "system.zip" }

```
## [#](zip.html#导入模块) 导入模块
 ```
import zip from '@system.zip'
// 或
const zip = require('@system.zip')

```
## [#](zip.html#接口定义) 接口定义
 ### [#](zip.html#zip-decompress-object) zip.decompress(OBJECT)
 解压文件
 #### [#](zip.html#参数) 参数：
 | 参数名  | 类型  | 必填  | 说明  | srcUri  | String  | 是  | 源文件的 uri，不能是 tmp 类型的 uri  | dstUri  | String  | 是  | 目标目录的 uri，不能是应用资源路径和 tmp 类型的 uri  | success  | Function  | 否  | 成功回调  | fail  | Function  | 否  | 失败回调  | complete  | Function  | 否  | 执行结束后的回调  #### [#](zip.html#success-返回值) success 返回值：
 无
 #### [#](zip.html#fail-返回值) fail 返回值：
 | 错误码  | 说明  | 202  | 参数错误  | 300  | I/O 错误  #### [#](zip.html#示例) 示例：
 ```
zip.decompress({
  srcUri: 'internal://cache/test.zip',
  dstUri: 'internal://files/unzip/',
  success: function() {
    console.log(`handling success`)
  },
  fail: function(data, code) {
    console.log(`handling fail, code = ${code}`)
  }
})

```


---
## 文件: guide\index.html

# # 概述

# [#](version/APILevel4.html#概述) 概述
 Xiaomi Vela JS 应用是小米公司开发的一种应用类型，它是基于小米的物联网嵌入式软件平台 Xiaomi Vela OS 开发的。本文将介绍 Xiaomi Vela JS 应用的特点、应用场景以及开发支持，帮助开发者更好地了解和使用这一应用类型。
 如果您想快速了解如何开发 Xiaomi Vela JS 应用，并且希望快速上手，请直接访问 [快速入门](start.html) 章节。
 ## [#](version/APILevel4.html#应用特点) 应用特点
 Xiaomi Vela JS 应用是一种基于 Xiaomi Vela OS 操作系统的轻量级应用模式，旨在为智能穿戴设备提供更加流畅和便捷的用户体验。它具备以下显著特点：
 
- **轻量化：**
Xiaomi Vela JS 应用采用了轻量级的架构设计，与传统的应用程序相比，Xiaomi Vela JS 应用具有更小的体积，这使得它们能够快速加载和运行，尤其适合内存和处理能力有限的穿戴设备。
 - **跨平台兼容性：**
Xiaomi Vela JS 应用支持跨端运行，开发者可以一次开发，实现在多种设备上的运行，这大大提高了开发效率和应用的普及率。
 - **高性能渲染：**
系统优化了渲染能力，使得应用的动画和交互更为流畅，提升了用户的使用体验。
 - **安全性能：**
Xiaomi Vela OS 通过三重隔离机制确保了应用的安全性，保护了用户数据和设备的安全。
 - **开发支持：**
小米提供了全面的开发支持 Xiaomi Vela JS 应用的开发工具和文档齐全，开发者可以轻松上手，快速构建高质量的应用。小米提供了包括AIoT-IDE在内的一系列开发工具，支持开发者在Ubuntu、Windows、MacOS等操作系统上进行Xiaomi Vela JS 应用的开发和调试。

 ## [#](version/APILevel4.html#应用场景) 应用场景
 Xiaomi Vela JS 应用的应用场景广泛，已落地多款产品，覆盖了智能穿戴设备上的多种使用情形：
 
- **健康监测：**  应用可以实时监测用户的心率、睡眠质量等健康数据，为用户提供健康建议和预警。
 - **运动辅助：**  在用户进行运动时，应用能够记录运动数据，提供运动指导和健康管理。
 - **消息提醒：**  应用能够显示手机等设备的消息提醒，方便用户在不拿出手机的情况下查看重要信息。
 - **移动支付：**  应用可以集成支付功能，用户可以直接在穿戴设备上完成支付操作，提高支付的便捷性。
 - **智能控制：**  作为智能家居的控制中心，应用可以远程操控家中的智能设备，如灯光、空调等。
 - **日常工具：**  提供天气预报、闹钟、计时器等日常工具功能，满足用户的多样化需求。

 ## [#](version/APILevel4.html#技术优势) 技术优势
 相较于传统的应用框架，Xiaomi Vela JS 应用具有以下技术优势：
 ### [#](version/APILevel4.html#前端开发范式) 前端开发范式
 Xiaomi Vela JS 应用采用JavaScript语言开发，并且支持前端MVVM高效的开发范式，响应式UI框架，易学易用。使得开发者可以降低上手难度，缩短开发周期。这种模式贴合主流前端开发者的思维习惯，使得开发者能够快速构建出功能丰富、交互友好的应用，同时降低了学习成本。参考[开发语法](framework/index.html)。
 ### [#](version/APILevel4.html#统一的api和组件) 统一的API和组件
 Xiaomi Vela JS 应用提供了统一的[JS接口](../features/index.html)和[UI组件](../components/index.html)，使得开发者无需关心底层硬件和操作系统的差异，简化了开发流程，同时保证了应用的质量和用户体验。
 ### [#](version/APILevel4.html#高性能渲染) 高性能渲染
 
- 通过架构优化，让复杂计算下沉到原生层，解决 JS 语言的性能瓶颈，从而拥有媲美原生的运行效率和流畅体验。
 - 提供丰富的动效能力，包括30+插值和物理动效，可用于过渡、转场等动画效果，使用户界面更加生动、有趣和富有表现力。
 - 充分挖掘硬件性能，最大限度利用 GPU 和 CPU 的硬件加速能力，让复杂UI 界面和动画更加流畅，达到 60 fps 满帧效果。

 ### [#](version/APILevel4.html#多屏适配) 多屏适配
 [多屏适配](multi-screens/index.html)是Xiaomi Vela JS 应用框架的另一大特色，具体表现在：
 
- **[适配规范](multi-screens/specs.html)：**  框架支持不同形状、尺寸和分辨率的屏幕自适应，确保应用在各种设备上都能提供良好的视觉体验。
 - **[设计规范](design/multi-screens.html)：**  Vela提供了一套多屏设计的技术规范，帮助开发者按照设计稿完成应用的多屏适配。
 - **[多屏UI模拟器](multi-screens/simulator.html)：**  AIoT-IDE提供了多屏UI模拟器，使开发者能够快速预览应用在不同屏幕上的效果，进行必要的调整。
 - **[适配案例](multi-screens/samples.html)：**  提供了对常见页面元素进行多屏适配的代码示例以及整站案例，供开发者参考和学习。

 ## [#](version/APILevel4.html#应用开发流程) 应用开发流程
 ### [#](version/APILevel4.html#环境搭建) 环境搭建
 小米提供了包括AIoT-IDE在内的一系列开发工具，支持开发者在Ubuntu、Windows、MacOS等操作系统上进行Xiaomi Vela JS 应用的开发和调试。
AIoT-IDE是Xiaomi Vela JS 应用的集成开发环境，提供了项目初始化、编码、调试等一系列功能。请参考[安装环境](start/use-ide.html)初始化项目。
 ### [#](version/APILevel4.html#应用开发) 应用开发
 初始化项目后请参考[项目结构](start/project-overview.html)了解项目中各文件和目录的作用。项目由配置文件（manifest.json）、模板代码（ux文件）、 样式代码（css文件）、逻辑代码（js文件）以及资源文件（图片、音频等）组成。请参考[项目配置](framework/manifest.html)对项目相关信息进行配置。
 应用开发范式遵循声明式UI，和传统Web开发范式类似。项目中的页面以及组件均由`ux`后缀文件编写，文件由[template 模板](framework/template/index.html)、[style 样式](framework/style/index.html)和[script 脚本](framework/script/index.html)3 个部分组成。开发者可以通过编写`ux`文件使用[UI组件](../components/index.html)、[自定义组件](framework/template/component.html)和样式描述页面结构和呈现效果，使用js脚本定义页面数据、实现生命周期接口、通用方法、事件处理等。
请参考[编写页面UI](start/user-interface.html)进一步了解页面开发。
 ### [#](version/APILevel4.html#运行调试) 运行调试
 AIoT-IDE提供内置的模拟器，支持开发者启动模拟器在IDE中直接运行和调试应用，实时查看运行效果。请参考[运行调试](start/use-ide.html#_5-运行项目.html)了解如何运行和调试应用。
 ### [#](version/APILevel4.html#打包发布) 打包发布
 应用开发完成后，开发者可以使用AIoT-IDE提供的打包功能将应用打包成安装包，请参考[打包项目](start/use-ide.html#_7-打包项目.html)了解如何打包应用。项目打包成功后请参考[发布](publish/index.html)进行应用发布。


---
## 文件: guide\start.html

# # 快速入门

# [#](start.html#快速入门) 快速入门
 这部分主要适合 Vela JS 应用开发的初学者。通过实现一个简单的天气预报APP，来熟悉 Vela JS 应用开发的流程和基础知识。


---
## 文件: guide\best-practice\business.html

# # 常用业务优化

# [#](business.html#常用业务优化) 常用业务优化
 ## [#](business.html#list与长文案优化) list与长文案优化
 说明
 list过长以及长文案（例如大段的隐私协议，用户协议）显示时，所能用到的优化手段

- list
 list列表，在初始化渲染时，原则上，如果内容超过10条，建议使用分页式渲染，或是触底发送请求新的数据，切勿初始化渲染大量数据，会造成页面渲染卡顿。

 - 长文案
 长文案一般是需要在小的设备屏幕上显示大量的文字内容，例如“用户协议”，“隐私协议”，“未成年人保护协议”等等
 
- 通常建议是用二维码显示协议链接，通过扫码在手机上浏览也是一种比较常用的设计。
 - 如果产品需要一次性全量渲染，会一定程度上造成页面的渲染卡顿，影响首次渲染的用户体验，这里推荐的是分块渲染文案。下面是代码示例：

 ```

      = 0}}">
        {{contentArray[0]}}
      
        = 1}}">
        {{contentArray[1]}}
      
        = 2}}">
        {{contentArray[2]}}

export default {
  data:{
    contentArray:[
      {
        content:'文案一..........'
      },
      {
        content:'文案二..........'
      },
      {
        content:'文案三..........'
      }
    ],
    //当前所需所渲染到的文案序列号
    currentKey:0,
    //当前总高度
    currentTHEight:0,
  }
  //onReady时先给当前总高度赋一次值
  onReady(){
    this.$element('content').getBoundingClientRect({
      success: (data) => {
        const { height } = data;
        this.currentTHEight = height
      }
    })
  }
  //实时判断滚动高度与总体高度，如果快触底了，则进行下一个文案的加载,同时给总高度重新赋值
  handleScroll(e) {
    if(currentTHEight - e.scrollY <40){
        this.currentKey = currentKey + 1
    }
    this.$element('content').getBoundingClientRect({
      success: (data) => {
        const { height } = data;
        this.currentTHEight = height
      }
    })
  }
}

```
## [#](business.html#swiper-多图优化) Swiper 多图优化
 说明
 当使用swiper轮播图时，如果图片很多。请避免同时渲染多张图片。保证可视区内以及左右图片的渲染即可

 假设现在一个相册有`200`张图片需要展示，就需要在`swiper`中创建`200`个子组件，无疑对性能是不友好的，因此考虑`swiper`只显示`3`个子组件，在左右滑动过程中动态更新子组件中的图片来实现`Swiper`中的数据懒加载。右滑懒加载主要过程如下：
 
- 右滑懒加载实现过程
假设有`5`张图片的数组为`data=[0,1,2,3,4]`，现在需要将这`5`张图片在含有`3`个子组件的`swiper`中展示。
 
1. 当用户点击第一张图，`swiper`中的数据为`data[0],data[1],data[2]`
 1. 当从第一张图片滑动到第二张图片的时候，`swiper`的数组仍然为`data[0],data[1],data[2]`
 1. 当从第2张滑动到第`3`张图片时，需要修改`swiper`第一个组件的数据为第三张图片的下一个数据`data[3]`，并且将`swiper`的`loop`属性设置为true，此时`swiper`的数据为`data[3],data[1],data[2]`；
 1. 当从`data[3]`滑动到`data[4]`时需要注意的是`data[4]`是最后一条数据，如果最后一条数据也不在`swiper`的最后一个组件中，需要将`swiper`中的所有数据进行重置为`data[len-3],data[len-2],data[len-1]`，以保证最后一张图片一定在`swiper`最后的一个组件中，并将`loop`设置为`false`，不允许从最后一张滑动到第一张。

 ![alt text](../../../images/components/business-swiper.jpg) `Swiper懒加载实例`
 
- 具体实现思路

 在代码中通过`@change`事件监听`swiper`的滑动。判断左滑右滑逻辑如下：
 ```
// 判断右滑
if (
  (!(this.currentIndex === 0 && index === length - 1) && index > this.currentIndex) ||
  (index === 0 && this.currentIndex === length - 1)
) {
}else{
}

```
右滑的逻辑如下：
 ```
//更新数据索引
this.dataIndex = this.dataIndex + 1
//更新下一次右滑的索引
const updateIndex = this.dataIndex + 1
if (updateIndex < this.bigThumbnailInfo.length) {
  //下一次右滑更新为当前的下一张
  updateItem = this.bigThumbnailInfo[updateIndex]
  // 如果滑动前是
  if (this.currentIndex === 0) {
    //未滑动前是第一张，右滑更新swiper的最后一个
    this.data[length - 1] = updateItem
    resIndex = length - 1
  } else {
    // console.info("右滑：更新左边的")
    this.data[this.currentIndex - 1] = updateItem
    resIndex = this.currentIndex - 1
  }
}

```
左滑代码逻辑如下：
 ```
//更新数据索引
this.dataIndex = this.dataIndex - 1
//更新下一次右滑的索引
const updateIndex = this.dataIndex - 1
//下一次左滑更新为当前的上一张
updateItem = this.bigThumbnailInfo[updateIndex]
if (this.currentIndex === length - 1) {
  //未滑动前在最后一张，左滑更新swiper第一个
  this.data[0] = updateItem
  resIndex = 0
} else {
  this.data[this.currentIndex + 1] = updateItem
  resIndex = this.currentIndex + 1
}

```
判断如果当前是最后一张图片代码如下：
 ```
this.data = [
  this.bigThumbnailInfo[len - 3],
  this.bigThumbnailInfo[len - 2],
  this.bigThumbnailInfo[len - 1]
]
indexTemp = 2
this.swiperIndex = this.currentIndex
this.isloop = false

```
判断即将更新的图片是第一张图片：
 ```
this.data = [
  this.bigThumbnailInfo[0],
  this.bigThumbnailInfo[1],
  this.bigThumbnailInfo[2]
]
indexTemp = 0
this.swiperIndex = this.currentIndex
this.isloop = false

```
如果不是第一张也不是最后一张图片，设置`swiper`的`loop`为`true`:
 ```
this.isloop = true

```
## [#](business.html#设备帧率的优化建议) 设备帧率的优化建议
 
- 有背景图或者图片的时候，尽量减少设置`border-radius`，使用带圆角的图片
 - 图片大小与`div`或者`image`组件大小保持一致，尽量不缩放图片
 - 减少`动态样式`修改
 - 减少标签的`嵌套层级`
 - 减少回流重绘

 ## [#](business.html#其他优化建议) 其他优化建议
 
- 增加try catch捕获异常
 - 数据请求较慢的场景建议增加loading


---
## 文件: guide\best-practice\index.html

# # 最佳实践

# [#](index.html#最佳实践) 最佳实践
 这部分主要介绍一些应用开发规范和最佳实践，如何降低应用时延和内存占用。帮助开发者开发更简洁、高性能、高扩展性的应用。


---
## 文件: guide\best-practice\memory.html

# # 内存优化

# [#](memory.html#内存优化) 内存优化
 由于运动手表整体内存较小，对于三方应用内存占用量要求比较高。根据之前遇到的问题，给出一份三方应用开发时的注意事项清单，以帮助开发者尽量降低应用的内存占用，符合手表应用验收标准。
 ## [#](memory.html#代码注意事项) 代码注意事项
 
1. 和 UI 无关，不需要绑定的数据，不要声明到 viewModel 的数据里，减少 observer 或者数据代理

 ```

    {{name}}     

  const someObj = { a: 1 } // 推荐写法
  export default {
    protected: {
      name: 'aaa',
      someObj: { // 不推荐写法
        a: 1
      }
    }
  }

```

1. 页面对象更新时，尽量原地更新，不要重新赋值，开辟新的内存空间

 ```
export default {
  protected: {
    list: [{
      name: 'aa',
      age: 22
    }]
  },
  
  onClick() {
    // 不推荐写法
    this.list = [{
      name: 'bb',
      age: 21
    }]
    // 推荐写法
    this.list[0].name = 'bb',
    this.list[0].age = 21
  }
}

```

1. 页面中声明的属性和方法不要缓存到全局上

 页面销毁时，为清理内存，会将页面对象相关的属性和方法尽量解除引用。如果被引用到全局，就无法清理其占用的内存，并且在其他地方调用该缓存的属性和方法，可能引起报错。
 ```
export default {
  protected: {
    list: [{
      name: 'aa',
      age: 22
    }]
  },
  
  onShow() {
    this.$app.$def.somearray.push(this.foo) // 不推荐写法
  }，
  
  foo() {
    this.list.push({
      name: 'bb',
      age: 21
    })
  }
}

```

1. 页面销毁时，清除未执行完的定时器

 ```
export default {
  protected: {
    timer: null
  }
  
  onShow() {
    this.timer = setTimeout(()=>{}, 1000000)
  }
  
  onDestroy() {
    if(this.timer){
      clearTimeout(this.timer)
    }
  }
}

```

1. 读取文件数据，用完后及时释放

 ```
let fileData; // 读取文件数据
let storageData; // 读取storage数据

file.readText({
  uri: 'internal://files/work/demo.txt',
  success: function(data) {
    fileData = data.text;
    console.log('text: ' + data.text)
  },
  fail: function(data, code) {
    console.log(`handling fail, code = ${code}`)
  }
});
storage.get({
  key: 'A1',
  success: function(data) {
    storageData = data;
    console.log('handling success')
  },
  fail: function(data, code) {
    console.log(`handling fail, code = ${code}`)
  }
})

// 用完后及时释放
fileData = null;
storageData = null;

```

1. 调用runGC方法

 通过执行全局对象global上的runGC方法，及时进行垃圾回收，避免内存泄漏。不要频繁调用，防止页面卡顿。
 ```
global.runGC()

```

1. `static`属性

 `template`模板中提供了`static`属性支持，如果绑定的变量后面不会再改变，添加`static`标记有助于框架减少实现动态节点，减少内存，也会降低页面销毁删除对象的时间。
 ```

    {{name}}

  export default {
    protected: {
      name: 'aaa'
    }
  }

```
另外，还支持在 `template` 上使用`.static`修饰符修饰节点的某个静态属性，适用于节点的该属性值仅在初始时被赋值一次，之后不会再变更。使用语法：`attr.static="{{ attrValue }}"`
 注意
 
- 节点的 `if` / `for` 静态属性只能通过 `.static` 修饰词进行修饰
 - 节点的 `static` 属性优先级比 `.static`高。对于声明了 `static` 属性的节点，可以不需要额外声明某个属性的静态修饰词 `attr.static`

 ```

      {{name}}

    <text
      if.static="{{ bool }}"
      style.static="{{ styl }}"
      someattr.static="{{ some }}"
      class.static="{{ cls }}"
      value.static
    >{{name}}

    <input
      if.static="{{ bool }}"
      style.static="{{ styl }}"
      someattr.static="{{ some }}"
      class.static="{{ cls }}"
      value.static="{{name}}"
    />

  export default {
    private: {
      name: 'aaa',
      bool: true,
      cls: 'basic-text',
      some: 'someattr',
      styl: {
        backgroundColor: '#d1d1d1'
      }
    }
  }

```
`block`组件是一个逻辑区块节点，如果增加了`static`属性，表示`block`包含的所有节点都是静态数据绑定，绑定的数据只计算一次，后面不会再发生改变，适用于绑定一些枚举值或者不可变的列表数据等，有效减少内存占用。
 ```

    标题： {{title}}
    条件渲染

        {{$item}}

  export default {
    private: {
      title: '我是标题1',
      cls: 'txt-cls',
      display: true,
      list: ['a', 'b', 'c']
    }
  }

```
## [#](memory.html#减少打包代码体积) 减少打包代码体积
 
1. 减少不必要的三方依赖，选用轻量的三方依赖

 对于`package.json`中的三方依赖，去除没有用到的依赖，对于必要的大型依赖，尽可能替换成轻量的依赖。
 
1. 使用全局方法

 将通用的方法、常量和对象实例统一挂在`global`上，在页面中不用再`import`，需要用的时候直接从`global`上取。
 ```
// global.js
import foo from './foo'
import bar from './bar'

global.foo = foo
global.bar = bar

// app.ux
import  './global'

// pages/xxx/index.js
const {foo, bar} = global

export default {
    // 调用foo、bar
    //......
}

```
以QQ音乐为例，以下为优化前后效果对比：
 |   | 优化前  | 替换轻量级依赖  | 使用全局方法  | 代码行数  | 21965  | 13156  | 6807  | 最大内存  | 4842844  | 3295928  | 1872528  
1. 在保证图片质量的前提下，尽量用低分辨率图片

大尺寸图片在加载时会占用较多内存，可以先将大尺寸图片缩放成小尺寸图片，再进行压缩([https://tinypng.com  (opens new window)](https://tinypng.com))，减少图片的体积。

 1. 去除没有用到的css和js

对于css中没有用到的样式，js中没有用到的变量和函数，可以删除或者注释，精简代码。

 1. 尽可能减少页面数量

在不影响业务需求的前提下，用最少的页面去实现，减少代码体积，简化应用逻辑。


---
## 文件: guide\best-practice\start.html

# # 启动时延优化

# [#](start.html#启动时延优化) 启动时延优化
 ## [#](start.html#避免settimeout延迟) 避免setTimeout延迟
 logo页如非必要，在执行页面跳转时，不要增加setTimeout延迟跳转。如果是需要等待异步结果返回，例如获取storage后决定跳转的下一个页面，建议将异步方法封装成同步，使用await，等待结果返回后立即执行跳转。以storage为例：
 ```
// ❌不推荐写法
onInit(){
  this.checkifHome()
  setTimeout(() => {
    if(!this.ifHome){
      router.push({uri:'pages/home'})
    }
  },1000)
}
checkifHome(){
  const that = this 
  storage.get({
    key: 'ifHome',
    success: function(data) {
      that.ifHome = data
    },
    fail: function(data, code) {
      console.log(`handling fail, code = ${code}`)
    }
  })
}

```
```
// ✅推荐写法一
onInit(){
  storage.get({
    key: 'ifHome',
    success: function(data) {
      if(!data){
        router.push({uri:'pages/home'})
      }
    },
    fail: function(data, code) {
      console.log(`handling fail, code = ${code}`)
    }
  })
}

```
```
// ✅推荐写法二
async onInit(){
  const ifHome = await checkifHome()
  if(!ifHome){
    router.push({uri:'pages/home'})
  }
}
checkifHome(){
  return new Promise((resolve, reject) => {
    storage.get({
      key: 'ifHome',
      success: function(data) {
        resolve(data) 
      },
      fail: function(data, code) {
        console.log(`handling fail, code = ${code}`)
        reject(code)
      }
    })
  })
}

```
```
// ✅推荐写法三
//可统一封装promise.js,方便其他异步接口复用
export function promisify(fn) {
  if (typeof fn !== 'function') {
    throw Error('[promisify] the type of `fn` should be function');
  }

  return (opts={}) => {
    let { success, fail, complete, ...args } = opts;

    if (typeof success === 'function' || typeof fail === 'function' || typeof complete === 'function') {
      console.warn('[promisify] [WARN] The `success`, `fail` and `complete` callback will be ignored');
    }

    return new Promise((resolve, reject) => {
      try {
        fn({
          ...args,
          success: data => resolve(data),
          fail: (data, code) => {
            let err = new Error(data);
            err.code = code;
            reject(err);
          }
        });
      } catch (error) {
        reject(error)
      }
    })
  }
}

//统一封装storage方法
import storage from '@system.storage';
import {promisify} from './promise';

const _get = promisify(storage.get);
const _set = promisify(storage.set);
const _clear = promisify(storage.clear);
const _delete = promisify(storage.delete);
export default {
  getItem(key) {
    return _get({key});
  },

  setItem(key, value) {
    return _set({key, value});
  },

  deleteItem(key) {
    return _delete({key});
  },

  clear() {
    return _clear();
  },
}

//logo.ux
async onInit(){
   const ifHome = await storage.getItem('ifHome')
  if(!ifHome){
     router.push({uri:'pages/home'})
  }
}

```
## [#](start.html#首页数据缓存) 首页数据缓存
 首页数据如果二次进入，需要再次展示的，可以考虑在应用（或首页）退出时增加上缓存，下次进入从logo页读取缓存后将数据存储在全局，首页page在onInit时直接读取，然后同时发起异步请求进行更新即可；
 ## [#](start.html#logo页避免http请求) logo页避免http请求
 建议不要在logo页引入http请求，尽可能放到首页执行，防止弱网或者无网情况阻塞页面跳转；
 ## [#](start.html#ui先行) UI先行
 如音乐类应用，进入应用建议默认状态为不播放，可以UI先行，如果歌曲信息获取成功立即展示，无需等到audio资源加载完成展示；
 ## [#](start.html#隐私页信息使用静态数据) 隐私页信息使用静态数据
 隐私页的数据代码里使用静态的数据，不用动态获取。需要展示长文本的，可以通过二维码扫码查看，二维码直接本地写死一个h5链接，不要通过接口去获取；
 ## [#](start.html#减少从console打印) 减少从console打印
 尽可能减少console打印，特别是长日志，很影响性能，避免很长的（>10行）console打印，尽可能减少json对象的打印，如果是debug期间需要打印日志，建议使用console.debug，并且配置quickapp.config.js（具体配置如下），在打release包的时候过滤掉console.debug的日志；
 ```
const TerserPlugin = require("terser-webpack-plugin")
const webpack = require("webpack")

module.exports = {
  postHook: (config) => {
    if (config.mode === "production") {
      config.optimization.minimize = true
      config.optimization.minimizer = [
        new TerserPlugin({
          terserOptions: {
            compress: {
              pure_funcs: ["console.debug"]
            }
          }
        })
      ]
    }
  }
}

```
## [#](start.html#图片缓存-裁剪) 图片缓存/裁剪
 如果有较大的（>100kb）动态图片，建议首次加载增加loading页，下载并缓存到本地，后续通过internal://files/XXX.png加载（重要：一般非必要不建议引入在线大图，引入的大图尺寸也不要超过屏幕尺寸，且大小不超过200kb，尽量使用本地图片代替在线图片，或者在线图片里支持resize-尺寸裁剪）
 ```
//login.ux
export function downloadFile(url) {// 下载图片
  return new Promise((resolve, reject) => {
    if(!url){
      resolve('')
    }
    request.download({
      url,
      success: function (ret) {
        const token = ret.token
        request.onDownloadComplete({
          token: token,
          success: function (ret) {
            console.info(`### request.download ### ret`,ret)
            resolve(ret.uri)
          },
          fail: function (msg, code) {
            console.info(`### request.onDownloadComplete ### ${code}: ${msg}`)
            resolve(null)
          }
        })
      }
    })
  })
}
const formUrl = 'http://XXX.cdn.homeBg.png'
downloadFile(formUrl).then(url => {
  global.homeBgUrl = url; //url => 'internal://files/homeBg.png'
})
 
//home.ux

//....
  computed:{
    bgImage() {
      const img =  global.homeBgUrl || 'http://XXX.cdn.homeBg.png'
      return img
    }
  }
//....
 
 //logo页
 global.homeBgUrl = await storage.getItem('homeBgUrl')
 
 //根据条件变化，及时进行图片清理
 logoOut(){
   file.delete({
    uri:global.homeBgUrl,
    success: function(data) {
      console.info(`###delFile sucess ${data}`)
      resolve(true)
    },
    fail: function(data, code) {
      resolve(false)
      console.log(`###delFile fail, code = ${code}`)
    }
  })
}

```
## [#](start.html#通信类应用通信之前使用diagnosis方法判断连接状态) 通信类应用通信之前使用diagnosis方法判断连接状态
 使用interconnect实现手表app和手机app的通信时，摒弃之前的轮询调用getApkStatus方法，改用新api [diagnosis](../../features/network/interconnect.html#connect-diagnosis-object)
 ```
data: {
   status: '',
   connectNum: 3,
   conn: null
},
onInit() {
   this.conn = interconnect.instance();
   this.connectStatus();
}, 

```
```
// ❌ 不推荐写法
connectStatus() {
  let status = this.conn.getApkStatus();
  if (status === 'CONNECTED' || this.connectNum === 0){
    this.status = status;
    // do something
  } else if (this.connectNum > 0) {
    this.connectNum --;
    setTimeout(() => {
      this.connectStatus()
    },500)
  }
}

```
```
// ✅推荐写法
connectStatus() {
  this.conn.diagnosis({
    success: (data) => {
      console.log(`handling success, status= ${data.status}`)
      // do something
    },
    fail: (data,code) => {
      console.log(`handling fail, code = ${code}`)
      // do something
    }
  })
}  

```
## [#](start.html#使用interconnect传输多条数据) 使用interconnect传输多条数据
 手表app向手机app传输多条数据时，若传输数量不大，建议直接一次性发送，无需增加延迟发送
 ```
// ❌不推荐写法
sendMsg(list) {
  for (let x in list) { 
    setTimeout(() => {
      this.conn.send({
        data: list[x],
        success: ()=>{ },
        fail: (data: {data, code})=> { }
      })
    },x*500) 
  }
}

```
```
// ✅推荐写法
sendMsg(list) {
  for (let x in list) {            
    this.conn.send({
      data: list[x],
      success: ()=>{ },
      fail: (data: {data, code})=> { }
    })
  }
}

```


---
## 文件: guide\design\index.html

# # 设计指南

# [#](index.html#设计指南) 设计指南
 vela 设备设计相关注意事项。
 
- [多屏设计](multi-screens.html)


---
## 文件: guide\design\multi-screens.html

# # 多屏设计

# [#](multi-screens.html#多屏设计) 多屏设计
 ## [#](multi-screens.html#小米智能穿戴设备) 小米智能穿戴设备
 目前搭载vela系统的小米可穿戴设备主要为智能手表、手环产品。手表屏幕形状为圆形或矩形，手环产品为矩形和胶囊型屏幕为主。
 已发布的vela穿戴设备数据参考：
 | 设备类型  | 设备型号  | 屏幕形状  | 屏幕尺寸  | 分辨率  | PPI  | DPR  | 手表  | Xiaomi Watch S1 Pro  | 圆形  | 1.47英寸  | 480x480  | 326  | 2.0  | 手表  | Xiaomi Watch H1  | 圆形  | 1.43英寸  | 466x466  | 326  | 2.0  | 手表  | Xiaomi Watch S3  | 圆形  | 1.43英寸  | 466x466  | 326  | 2.0  | 手表  | Xiaomi Watch S4 sport  | 圆形  | 1.43英寸  | 466x466  | 326  | 2.0  | 手表  | Xiaomi Watch S4  | 圆形  | 1.43英寸  | 466x466  | 326  | 2.0  | 手表  | REDMI Watch 5  | 矩形  | 2.07英寸  | 432x514  | 324  | 2.0  | 手环  | 小米手环8 Pro  | 矩形  | 1.74英寸  | 336x480  | 336  | 2.1  | 手环  | 小米手环9  | 跑道型胶囊屏  | 1.62英寸  | 192x490  | 325  | 2.0  | 手环  | 小米手环9 Pro  | 矩形  | 1.74英寸  | 336x480  | 336  | 2.1  | 手环  | 小米手环10  | 胶囊形  | 1.725英寸  | 212x520  | 326  | 2.0  ## [#](multi-screens.html#设计建议) 设计建议
 产品接入时可以根据应用场景及可适配的产品形态来做设计决策。若所属产品场景在手环、手表等多种屏幕形态都能很好的交互，建议出三类设计稿满足胶囊形、圆形、矩形屏的交互方案。
 不同形状屏幕数据参考：
 | 屏幕形状  | 圆屏  | 矩形屏  | 胶囊屏  | 长宽比范围  | W/H=1  | 0.5<=W/H<1  | 0.3<W/H<0.5  | 推荐长宽比例  | 1  | 0.7  | 0.39  | 推荐分辨率  | 466x466  | 336x480  | 192x490  推荐设计三套UI交互适配三类主要屏幕，若圆屏矩形屏能够复用的话可以设计圆形、矩形屏采用一套，胶囊屏采用一套。
 ## [#](multi-screens.html#弧形屏幕适配安全区域) 弧形屏幕适配安全区域
 对于圆形以及胶囊形屏幕，弧形的屏幕边缘会带来一些显示问题，在UI设计时需要考虑屏幕的安全区域问题，将主体功能设计在屏幕安全区域内。
 比如，文本显示或内容列表，需要考虑边缘位置的显示完整性和可交互性。
 图示里灰色区域分别为圆屏、胶囊屏安全区。
 ![](../../../images/multi-screens/multi-safe-area.png)


---
## 文件: guide\developer-materials\extension-components.html

# # 拓展组件

# [#](extension-components.html#拓展组件) 拓展组件
 ## [#](extension-components.html#input-method) input-method
 ### [#](extension-components.html#概述) 概述
 输入法组件，适配不同屏幕形状，支持两种主流键盘布局——全键盘模式和九键模式下的中英文输入
 ### [#](extension-components.html#示例预览) 示例预览
 #### [#](extension-components.html#全键盘模式) 全键盘模式
 
- 圆形屏幕

 ![](../../../images/guide/input-method-qwerty.png)
 
- 矩形屏幕

 ![](../../../images/guide/input-method-qwerty-rect.png)
 
- 胶囊屏幕

 ![](../../../images/guide/input-method-qwerty-pill-shaped.png)
 #### [#](extension-components.html#九键模式) 九键模式
 
- 圆形屏幕

 ![](../../../images/guide/input-method-t9.png)
 ### [#](extension-components.html#项目地址) 项目地址
 
- [Github  (opens new window)](https://github.com/NEORUAA/Vela_input_method)


---
## 文件: guide\developer-materials\index.html

# # 开发者物料

# [#](index.html#开发者物料) 开发者物料
 开发者物料表示快应用平台为开发者提供的各种资源和工具，当前包含拓展组件，未来会增加页面模板、功能接口、开发工具等更多支持。欢迎每一位开发者与我们联系[vela-appframework@xiaomi.com](mailto:vela-appframework@xiaomi.com)，分享您的需求或贡献您的物料资源，共同推动技术的进步和创新，建设更加繁荣的快应用生态。
 拓展组件本质是开发者编写的自定义组件，其他开发者如果想要使用拓展组件，只需将项目地址中的组件文件夹拷贝到自己的工程目录中，通过``方式引入到页面中使用。同时，拓展组件为开发者提供丰富的属性配置和回调事件。
 拓展组件的这种设计方式可以在防止页面与组件产生不必要耦合关系的规则下，保证页面对组件的管理能力，帮助开发者更好地组织逻辑与代码，提升项目的可维护性。


---
## 文件: guide\framework\index.html

# # 框架简介

# [#](index.html#框架简介) 框架简介
 本应用框架是一套以前端开发技术栈为主进行应用开发的框架，采用流行的前端开发模式，贴合主流前端开发者的思维习惯，同时大幅提升应用的性能，提供大量前端环境无法使用的系统能力，以及很多第三方服务的对接能力。
 ## [#](index.html#文件结构) 文件结构
 应用由一个 manifest.json 和多个页面 ux 文件组成。manifest.json 文件中定义应用描述、功能权限声明、系统配置和页面路由等信息；页面 ux 文件中完成单个页面的具体实现，包括 UI 模板、样式单、数据定义和回调事件处理等。具体使用参看[项目结构](project-structure.html)。
 ## [#](index.html#应用框架) 应用框架
 ### [#](index.html#路由管理) 路由管理
 框架负责管理整个应用的页面路由，实现页面间的无缝切换，管理每个页面的完整生命周期。开发者需要将页面在 manifest.json 中进行注册，在代码中通过框架提供的接口方法实现页面的切换。具体使用参看[项目配置](manifest.html)、[页面切换](page-switch.html)。
 ### [#](index.html#数据绑定) 数据绑定
 数据绑定可以让数据与视图非常简单地保持同步。当做数据修改的时候，只需要在逻辑层修改数据，视图层就会做相应的更新。数据绑定的具体使用参看[模板语法](template/index.html)。
 ### [#](index.html#界面组件) 界面组件
 框架提供了一套基础的界面组件，界面组件标签除了支持常用的 HTML5 标签，例如`，`等之外，还提供与原生 UI 相关的组件标签，例如`，，`等。具体使用参看[组件](../../components/index.html)。
 ### [#](index.html#原生接口) 原生接口
 框架还提供丰富的原生接口，既有通用的系统功能，也有第三方服务的对接，如网络请求、本地存储等等。这些 API 可以大大节省开发者工作量，快速开发出应用。具体使用参看[接口](../../features/index.html)。


---
## 文件: guide\framework\manifest.html

# # 项目配置

# [#](manifest.html#项目配置) 项目配置
 `manifest.json`文件中包含了应用描述、接口声明、页面路由信息。
 ## [#](manifest.html#manifest) manifest
 | 属性  | 类型  | 默认值  | 必填  | 描述  | package  | String  | -  | 是  | 应用包名，**确认与原生应用的包名不一致**，推荐采用 com.company.module 的格式，如：com.example.demo  | name  | String  | -  | 是  | 应用名称，**6 个汉字以内，与应用商店保存的名称一致**，用于在桌面图标、弹窗等处显示应用名称  | icon  | String  | -  | 是  | 应用图标，提供 192x192 大小的即可  | versionName  | String  | -  | 否  | 应用版本名称，如："1.0"  | versionCode  | Integer  | -  | 是  | 应用版本号，从`1`自增，**推荐每次重新上传包时`versionCode`+1**  | minAPILevel  | Integer  | 1  | 否  | 支持的最小 API 标准版本号，**兼容性检查，避免上线后在低版本平台运行并导致不兼容**；如果不填按照内测版本处理  | features  | Array  | -  | 否  | 接口列表，绝大部分接口都需要在这里声明，否则不能调用，详见每个接口的文档说明  | config  | Object  | -  | 是  | 系统配置信息，详见下面说明  | router  | Object  | -  | 是  | 路由信息，详见下面说明  | display  | Object  | -  | 否  | UI 显示相关配置，详见下面说明  | deviceTypeList  | Array  | watch  | 否  | 可选值有：watch、tv、car、phone，现只支持watch  | permissions  | Array  | -  | 否  | 权限申请，示例：[{ "name": "hapjs.permission.LOCATION" }]  ### [#](manifest.html#config) config
 用于定义系统配置和全局数据。
 | 属性  | 类型  | 默认值  | 描述  | logLevel  | String  | log  | 打印日志等级，分为 off、error、warn、info、log、debug  | designWidth  | Integer  | -  | 页面设计基准宽度，根据实际设备宽度来缩放元素大小  | background  | Object  | -  | 后台运行配置信息，可使用 features 字段申请需要在后台使用的接口（同时仍需在最外层的 features 字段中声明）。可申请的接口为：
 system.audio 
 system.geolocation 
 system.request 等 
  详细用法参见 [后台运行](other/background-running.html) 脚本  ### [#](manifest.html#minapilevel) minAPILevel
 支持的最小 API 标准版本号，标识开发者的 rpk 包能兼容运行在最小实现了该版本 API 标准的设备上，其值默认为1。当使用了 1 及以上的 API 标准版本新增特性时，就必须确保 minAPILevel 最低为该版本号，避免上线后在实现了更低版本 API 标准的设备上运行出错。
 示例如下：
 ```
{
  "minAPILevel": 1
}

```
### [#](manifest.html#router) router
 用于定义页面的组成和相关配置信息，如果页面没有配置路由信息，则在编译打包时跳过。
 | 属性  | 类型  | 默认值  | 必填  | 描述  | entry  | String  | -  | 是  | 首页名称，使用分包功能时，建议将首页定义在基础包  | pages  | Object  | -  | 是  | 页面配置列表，key 值为页面名称（对应页面目录名，例如 Hello 对应'Hello'目录），value 为页面详细配置 page，详见下面说明  示例代码：
 ```
"router": {
  "entry": "Demo",
  "pages": {
    "Demo": {
      "component": "index"
    }
  }
}

```
#### [#](manifest.html#router-pages) router.pages
 用于定义单个页面路由信息。
 | 属性  | 类型  | 默认值  | 必填  | 描述  | component  | String  | -  | 是  | 页面对应的组件名，与 ux 文件名保持一致，例如"hello" 对应 "hello.ux"  | path  | String  | /  | 否  | 页面路径，例如"/user",不填则默认为/
path 必须唯一，不能和其他 page 的 path 相同
下面 page 的 path 因为缺失，会被设置为"/Index"：
`"Index": {"component": "index"}`  | launchMode  | String  | standard  | 否  | 声明页面的启动模式，支持"singleTask"，"standard"两种页面启动模式
标识为"singleTask"模式时每次打开目标页面都会打开已有的目标页面并回调 onRefresh 生命周期函数，清除该页面上打开的其他页面，没有打开过此页面时会创建新的目标页面实例
标识为"standard"模式时会每次打开新的目标页面（多次打开目标页面地址时会存在多个相同页面）  ### [#](manifest.html#示例代码) 示例代码
 ```
{
  "package": "com.company.unit",
  "name": "appName",
  "icon": "/Common/icon.png",
  "versionName": "1.0",
  "versionCode": 1,
  "minPlatformVersion": 1000,
  "features": [{ "name": "system.network" }],
  "router": {
    "entry": "Hello",
    "pages": {
      "Hello": {
        "component": "hello",
        "path": "/"
      }
    }
  }
}

```
### [#](manifest.html#display) display
 用于定义与 UI 显示相关的配置。
 如果在 display 对象下定义以下属性值，则生效范围为此JS 应用全部页面；
 | 属性  | 类型  | 默认值  | 描述  | backgroundColor  | String  | #ffffff  | 窗口背景颜色  ### [#](manifest.html#权限说明) 权限说明
 | 权限名  | feature  | api  | 描述  | 权限错误码  | hapjs.permission.LOCATION  | system.geolocation  | getLocation 
 subscribe 
 unsubscribe  | 地理位置  | 400： 拒绝授予权限 
 402： 权限错误（未声明该权限）  | hapjs.permission.DEVICE_INFO  | system.device  | getSerial 
 getDeviceId  | 获取设备信息  | 400： 拒绝授予权限 
 402： 权限错误（未声明该权限）


---
## 文件: guide\framework\page-switch.html

# # 页面切换

# [#](page-switch.html#页面切换) 页面切换
 ## [#](page-switch.html#通过接口-router-切换页面和传递参数) 通过接口 router 切换页面和传递参数
 ### [#](page-switch.html#切换页面) 切换页面
 router 接口在使用前，需要先导入模块。
 通过`router.push(OBJECT)`可以完成页面切换，其支持的参数`uri`的格式详细描述参见[页面路由](../../features/basic/router.html)。
 **示例如下：**
 ```

  .page {
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }
  .btn {
    width: 400px;
    height: 60px;
    margin-top: 70px;
    border-radius: 30px;
    background-color: #09ba07;
    font-size: 30px;
    color: #ffffff;
  }

  // 导入模块
  import router from '@system.router'

  export default {
    routePage () {
      // 跳转到应用内的某个页面，当前页面无法返回
      router.replace({
        uri: '/Pages/newPage'
      })
    }
  }

```
### [#](page-switch.html#传递参数) 传递参数
 `router`接口的参数`params`可配置页面跳转时需要传递的参数。
 **示例如下：**
 ```

  .page {
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }
  .btn {
    width: 400px;
    height: 60px;
    margin-top: 70px;
    border-radius: 30px;
    background-color: #09ba07;
    font-size: 30px;
    color: #ffffff;
  }

  // 导入模块
  import router from '@system.router'

  export default {
    private: {
      title: 'Hello, world!'
    },

    onInit () {
      console.info('接口router切换页面并传递参数')
    },

    routePageReplaceWithParams () {
      // 跳转到应用内的某个页面
      router.replace({
        uri: '/PageParams/receiveparams',
        params: { key: this.title }
      })
    }
  }

```
## [#](page-switch.html#接收参数) 接收参数
 现在，开发者已经了解了通过接口`router`在页面之间传递参数的方法，如何接收参数呢？
 其实很简单，接口`router`传递的参数的接收方法完全一致：在页面的 ViewModel 的`protected`属性中声明使用的属性。
 注意
 
- `protected`内定义的属性，允许被应用内部页面请求传递的数据覆盖，不允许被应用外部请求传递的数据覆盖
 - 若希望参数允许被应用外部请求传递的数据覆盖，请在页面的 ViewModel 的`public`属性中声明使用的属性

 **示例如下：**
 ```

    page
    
    {{key}}

  .page {
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }

  export default {
    protected: {
      key: ''
    },
    onInit () {
      // js中输出页面传递的参数
      console.info('key: ' + this.key)
    }
  }

```


---
## 文件: guide\framework\project-structure.html

# # 项目结构

# [#](project-structure.html#项目结构) 项目结构
 ## [#](project-structure.html#应用资源) 应用资源
 一个应用包含：描述项目配置信息的[manifest 文件](manifest.html)，放置项目公共资源脚本的[app.ux 文件](ux.html)，多个描述页面的[ux 文件](ux.html)，典型示例如下：
 应用根目录：
 ```
├── manifest.json
├── app.ux
├── pages
│   ├── index
|   |   └── index.ux
│   └── detail
|       └── detail.ux
├── i18n
|   ├── defaults.json
|   ├── zh-CN.json
|   └── en-US.json
└── common
    ├── style.css
    ├── utils.js
    └── logo.png

```
## [#](project-structure.html#ux模板) ux模板
 一个页面通常都由三部分组成：页面结构、样式和逻辑交互。这三部分，可以放在一个ux文件中，也可以作为独立的文件。
 如果放在一个ux文件中，则ux文件需要包含三标签：`template`、`style`和`script`。
 示例：
 ```

    欢迎打开{{title}}

  .btn {
    width: 400px;
    height: 60px;
    background-color: #09ba07;
    color: #ffffff;
  }

  import router from '@system.router'

  export default {
    // 页面数据对象
    private: {
      title: '示例页面'
    },
    // 按钮点击后的回调
    routeDetail() {
      router.push({
        uri: '/pages/detail'
      })
    }
  }

```
如果将页面结构、样式和逻辑交互分开作为独立的文件，可以使用如下目录结构：
 ```
├── ...
├── pages
│   ├── ...
│   └── detail
|       ├── detail.ux
|       ├── detail.css
|       └── detail.js
├── ...

```
说明
 如果作为独立的文件，将ux/css/js文件分开后，ux文件中不能包含`template`标签。

 ## [#](project-structure.html#文件存储) 文件存储
 在应用平台中是按分区来存储文件的，目前支持以下分区：
 
1. Cache，一般用于存储缓存文件，比如通过 fetch 接口下载的文件会存储在该分区中，该分区中的文件可能因存储空间不够被系统删除；
 1. Files，一般用于存储比较小的永久文件，该分区中的文件由应用自己管理；
 1. Mass，一般用于存储比较大的文件，但该分区并不保证一直可用；
 1. Temp，表示从外部映射过来的临时文件，出于安全性考虑，临时文件是只读的，并且只能通过调用特定的 API 获取，比如 file.readText 方法。另外临时文件的访问是临时的，应用重启后无法访问到临时文件，需要通过特定 API 重新获取。

 另外应用资源也作为一个特殊的只读分区进行处理。
 ## [#](project-structure.html#uri) URI
 URI 用于标识应用资源和文件，[组件](../../components/index.html)和[接口](../../features/index.html)通过 URI 来访问应用资源和文件。
 | 资源类型  | URI  | 只读  | 示例  | 说明  | 应用资源  | /path  | 是  | /Common/header.png  | -  | Cache  | internal://cache/path  | 否  | internal://cache/fetch-123456.png  | -  | Files  | internal://files/path  | 否  | internal://files/image/demo.png  | -  | Mass  | internal://mass/path  | 否  | internal://mass/video/demo.mp4  | -  | Temp  | internal://tmp/path  | 是  | internal://tmp/xxxxx  | 由系统动态生成  URI 允许的字符是`0-9a-zA-Z_-./%:`(不包含引号)，URI 中不能出现`..`，URI 支持目录结构，目录由斜线'/'分隔。
 internal URI 表示的是应用私有文件，即在指定 internal URI 时，无需指定应用标识，同一个 internal URI 对于不同的应用会指向不同的文件。
 ## [#](project-structure.html#资源和文件访问规则) 资源和文件访问规则
 应用资源路径分为绝对路径和相对路径，以"/"开头的路径表示绝对路径，比如 /Common/a.png，不以"/"开头的路径是相对路径，比如 a.png 和 ../Common/a.png 等。
 应用资源文件分为代码文件和资源文件，代码文件是指 .js/.css/.ux 等包含代码的文件，其他文件则是资源文件，这类文件一般只当作数据来使用，比如图片、视频等。
 
1. 在代码文件中，导入其他代码文件时，使用相对路径，比如：../Common/component.ux；
 1. 在代码文件中，引用资源文件(如：图片、视频等)时，一般情况下使用相对路径，比如：./abc.png；
 1. 当代码文件需要被导入时，如果导入文件与被导入文件在同一个目录，被导入文件引用资源文件时可以使用相对路径，但如果不在同一目录，必须使用绝对路径，因为被导入文件编译时会被复制到导入文件中，编译后目录会发生变化。比如 a.css 文件被 b.ux 导入，如果 a.css 与 b.ux 在同一个目录，a.css 引用资源文件时可以写相对路径：abc.png，如果不在同一个目录，必须写绝对路径：/Common/abc.png，再比如当 a.ux 文件被 b.ux 文件导入时，如果 a.ux 与 b.ux 在同一个目录，a.ux 引用资源文件时可以写相对路径：a.png，如果不在同一个目录，a.ux 引用资源必须写绝对路径：/Common/abc.png；
 1. 在 CSS 中，与前端开发一致，使用 url(PATH)的方式访问资源文件，如：url(/Common/abc.png)。


---
## 文件: guide\framework\ux.html

# # UX 文件

# [#](ux.html#ux-文件) UX 文件
 APP，页面均通过 ux 后缀文件编写，ux 后缀文件由[template 模板](template/index.html)、[style 样式](style/index.html)和[script 脚本](script/index.html)3 个部分组成，一个典型的页面 ux 后缀文件示例如下：
 ```

    欢迎打开{{title}}

  .page {
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }

  .title {
    font-size: 30px;
    text-align: center;
  }

  .btn {
    width: 400px;
    height: 60px;
    margin-top: 75px;
    border-radius: 43px;
    background-color: #09ba07;
    font-size: 30px;
    color: #ffffff;
  }

  import router from '@system.router'

  export default {
    // 页面级组件的数据模型，影响传入数据的覆盖机制：private内定义的属性不允许被覆盖
    private: {
      title: '示例页面'
    },
    routeDetail () {
      // 跳转到应用内的某个页面，router用法详见：文档->接口->页面路由
      router.push ({
        uri: '/DemoDetail'
      })
    }
  }

```
## [#](ux.html#app-ux) app.ux
 当前`app.ux`编译后会包含`manifest配置信息`（可以在`npm run build`之后查看文件内容），所以请不要删除`/**manifest**/`的注释内容标识。
 您可以在``中引入一些公共的脚本，并暴露在当前 app 的对象上，如下所示，然后就可以在页面 ux 文件的 ViewModel 中，通过`this.$app.$def.util`访问。
 ```

  /**
   * 应用级别的配置，供所有页面公用
   */
  import util from './util'

  export default {
    showMenu: util.showMenu,
    createShortcut: util.createShortcut,
    util
  }

```


---
## 文件: guide\framework\other\background-running.html

# # 后台运行

# [#](background-running.html#后台运行) 后台运行
 为了节省系统资源，通常情况下，应用切换到后台后将会停止运行，等到再次切换回前台时重新运行。但音乐\运动等类型的应用，退到后台后可能仍然需要继续运行，为满足此类需求，加入了对后台运行的支持。后台运行模式的工作原理如下：
 在应用切换到后台时，系统将会检查是否满足后台运行的条件，如果满足，应用将继续运行，否则将被停止。此条件包括：
 
1. `manifest.json`中声明了后台运行接口

 1. 当前至少有一个（已在`manifest.json`中声明的）后台运行接口正在运行

 实践建议：
 
- 后台运行需要消耗较多的系统资源，应用需要根据自身需求审慎使用。针对申请后台运行的应用，上线审核时将会审核其后台运行的需求是否合理。
 - 后台运行接口的导入和后台执行的工作放到`app.ux`中，而不是放到页面中，以免避免页面切换和销毁的影响。

 ## [#](background-running.html#配置方法) 配置方法
 manifest.json 中声明所需的后台运行接口。后台运行接口包括：
 
1. 音频播放： `system.audio`
 1. 上传下载： `system.request`
 1. 地理位置： `system.geolocation`

 ```
{
  "package": "com.hybrid.demo.sample",
  //  ......

  "config": {
    "logLevel": "trace",
    "background": {
      "features":[
        "system.audio",
        "system.request"
      ]
    }
  }
  //  ......
}

```


---
## 文件: guide\framework\other\dynamic-component.html

# # 动态组件

# [#](dynamic-component.html#动态组件) 动态组件
 提示
 通过本节，你将学会如何使用动态组件，减少模板的代码量，提高代码的可读性。

 当页面中引入多个组件并需要动态切换组件时，绝大数情况下推荐在模板上使用 `if` 等指令进行逻辑判断，改变视图结构。
 **示例如下：**
 ```

  export default {
    data() {
      return {
        status: 1
      }
    }
  }

```
但当组件较多时，模板的代码量会变得很大，不利于维护。此时可以使用 **动态组件** 来减少模板的代码量，通过在 `` 元素加一个特殊的 `is` 属性来实现，`is` 的值表示组件名，只需修改 `is` 属性即可切换组件。
 **示例如下：**
 ```

  export default {
    data() {
      return {
        status: 1
      }
    }
  }

```


---
## 文件: guide\framework\other\hap-schema.html

# # hap 链接

# [#](hap-schema.html#hap-链接) hap 链接
 hap链接 指在router模块中支持的以hap://开头的uri，使用场景见[页面路由](../../../features/basic/router.html)。
 目前支持的 hap 链接以`hap://app/`开头，支持打开指定的JS 应用，格式如下：
 `hap://app//[path][?key=value]`
 参数说明：
 
- package: 应用包名，必选
 - path: 应用内页面的 path，可选，默认为首页
 - key-value: 希望传给页面的参数，可选，可以有多个


---
## 文件: guide\framework\other\i18n.html

# # 多语言覆盖

# [#](i18n.html#多语言覆盖) 多语言覆盖
 Vela 的能力会覆盖多个国家地区，框架支持多语言的能力后，可以做到让一个JS 应用产品（一个 RPK 文件）同时支持多个语言版本的切换，开发者无需开发多个不同语言的源码项目，避免给项目维护带来困难。
 使用系统默认的语言，开发者配置多语言的方式非常简单，只需要`定义资源`与`引用资源`两个步骤即可。
 ## [#](i18n.html#定义资源文件) 定义资源文件
 资源文件用于存放多个语言的业务信息定义，与其它技术平台类似（它们使用`properties文件`或者`xml文件`的格式），JS 应用平台使用`JSON文件`保存资源定义；
 在项目源码`src目录`下定义`i18n文件夹`，内部放置每个语言地区下的资源定义文件即可。
 ### [#](i18n.html#资源文件命名查找规则及建议) 资源文件命名查找规则及建议
 文件命名可使用当前系统获取到的语言以及国家信息，例如文件名可定义为：`zh-CN.json`、`zh.json`。
 如果开发者当前产品仅计划支持一种语言，同时还希望用到多语言能力，那么仅声明一个名称为`defaults.json`的文件即可。
 JSON文件名匹配优先级，优先级高的匹配到就停止查找，否则就向下一级查找。
 **优先级匹配规则**如下：
 `-`
 ``
 `defaults`
 默认i18n配置文件首个文件
 **命名建议**：
 
- 如果需要精确匹配语言+地区进行多语言配置建议使用`-.json`命名资源文件；

 - 不需要匹配地区的建议使用`.json`命名资源文件；

 - `defaults.json`可以作为默认选项单独使用，也可配合以上两种方式结合使用；

 - 不推荐使用系统最终的兜底默认首个文件的方案，可能会造成不符合预期的显示结果。

 温馨提示
 `-` 可参考：[支持的语言列表](language-list.html)。

 ### [#](i18n.html#资源文件支持的配置语法) 资源文件支持的配置语法
 #### [#](i18n.html#基础文本配置) 基础文本配置
 ```
{
  "message": {
    "hello": "hello world"
  }
}

```
#### [#](i18n.html#数组配置) 数组配置
 匹配到会把数据内容序列化转成文本输出，此种配置不支持与插值语法混用。
 ```
{
  "message": {
    "array": ["a", 2, {"c": 3}]
  }
}

```
#### [#](i18n.html#命名插值配置) 命名插值配置
 支持使用`{}`占位符进行命名插值，调用时通过具名参数传入替代占位内容。
 ```
{
  "message": {
    "hello": "{msg} world"
  }
}

```
#### [#](i18n.html#列表插值配置) 列表插值配置
 支持使用`{}`占位符进行列表插值，通过配置列表取值索引，在调用时传入备选列表进行取值替代占位内容。
 ```
{
  "message": {
    "hello": "{0} world"
  }
}

```
#### [#](i18n.html#单复数语法配置) 单复数语法配置
 支持使用`|`占位符进行单复数语法配置，不同的选择项使用占位符分隔。
 ```
{
  "message": {
    car: 'car | cars',
  }
}

```
## [#](i18n.html#页面中引用资源) 页面中引用资源
 使用多语言配置的方式主要通过ViewModel 实例上`$t`与`$tc`函数实现，这些方法可以在``或``中使用。
 ### [#](i18n.html#简单格式化方法) 简单格式化方法
 this.$t(path, opts)
 **参数说明**：
 | 参数  | 类型  | 是否必填  | 说明  | path  | String  | 是  | 获取多语言配置的资源路径，对象取值通过.连接，例如：”message.hello“  | opts  | Array | Object  | 否  | 进行插值替换的配置项，可以传入对象或数组，配合配置中的差值配置使用
若传入对象则需要指定配置的命名key进行传参
若传入数组取值为传入列表对应的列表插值配置的index值  **使用示例**：
 使用**基础文本配置**对应取值示例：
 ```

    {{ $t('message.hello') }}

  export default {
    onInit () {
      // 简单格式化：
      console.log(this.$t('message.hello')) // hello world
    }
  }

```
使用**数组配置**对应取值示例：
 ```

    {{ $t('message.array') }}

  export default {
    onInit () {
      // 简单格式化：
      console.log(this.$t('message.array')) // ["a", 2, {"c": 3}]
    }
  }

```
使用**命名插值配置**对应取值示例：
 ```

    {{ $t('message.hello', { msg: 'hello' }) }}

  export default {
    onInit () {
      // 简单格式化：
      console.log(this.$t('message.hello', { msg: 'hello' }))  // hello world
    }
  }

```
使用**列表插值配置**对应取值示例：
 ```

    {{ $t('message.hello', ['hello', 'hi']) }}

  export default {
    onInit () {
      // 简单格式化：
      console.log(this.$t('message.hello', ['hello', 'hi'])) // hello world
    }
  }

```
### [#](i18n.html#单复数格式化方法) 单复数格式化方法
 this.$tc(path, choice, opts)
 **参数说明**：
 | 参数  | 类型  | 是否必填  | 说明  | path  | String  | 是  | 获取多语言配置的资源路径，对象取值通过.连接，例如：”message.hello“  | choice  | Number  | 否  | 用于判断使用第几个选项的值，不传具体值时默认单数，在不传入第三个参数时也可用作插值显示
**特殊值说明**：
值必须是整数，错参报错不显示返回空字符串
单复数判断忽略正负符号
两段式0作为偶数处理  | opts  | Array | Object  | 否  | 进行插值替换的配置项，可以传入对象或数组，配合配置中的差值配置使用
若传入对象则需要指定配置的命名key进行传参
若传入数组取值为传入列表对应的列表插值配置的index值  **choice单复数配置说明**：
 目前单复数在资源文件中支持两种写法并支持与插值语法混用。
 两段式配置：单数|复数；
 三段式配置：空值|单数|复数。
 **配置示例**：
 ```
{
  "message": {
    car: 'car | cars', // 两段式配置
    apple: 'no apples | one apple | {count} apples' // 三段式配置
  }
}

```
**使用示例**：
 ```

    {{ $tc('message.car', 0) }}
    
    {{ $tc('message.car', 1) }}
    
    {{ $tc('message.car', 2) }}

    {{ $tc('message.apple', 0) }}
    
    {{ $tc('message.apple', 1) }}
    
    {{ $tc('message.apple', 2) }}

    {{ $tc('message.apple', 2, {count: 6}) }}

  export default {
    onInit () {
      // 两段单复数：
      console.log(this.$tc('message.car', 0)) // cars
      console.log(this.$tc('message.car', 1)) // car
      console.log(this.$tc('message.car', 2)) // cars

      // 三段单复数：
      console.log(this.$tc('message.apple', 0)) // no apples
      console.log(this.$tc('message.apple', 1)) // one apple
      console.log(this.$tc('message.apple', 2)) // 2 apples
      console.log(this.$tc('message.apple', 2, {count: 6})) // 6 apples
    }
  }

```
## [#](i18n.html#获取系统语言) 获取系统语言
 上面的能力用于资源内容的格式化，在某些场景下开发者可能需要获取当前系统的地区语言`locale`并进行更改，来完成不同的逻辑处理。比如：
 
- 不同的 locale 对应的页面布局不同；

 - 开发者为用户提供设置某种语言的能力；

 框架`system.configuration`提供了相关功能，文档参考：[应用配置 configuration](../../../features/basic/configuration.html)。
 ## [#](i18n.html#修改地区语言后的回调) 修改地区语言后的回调
 当用户在系统设置切换地区语言，会触发 onConfigurationChanged 回调，且返回来的 event.type 值为locale。
 详情可参考[文档](../script/lifecycle.html#onconfigurationchangedevent)。
 示例代码：
 ```
// 监听语言变化
onConfigurationChanged(event) {
  if (event && event.type && event.type === 'locale') {
    console.log('locale or language changed!')
  }
}

```


---
## 文件: guide\framework\other\index.html

# # 框架扩展能力

# [#](index.html#框架扩展能力) 框架扩展能力
 框架其他扩展能力。
 
- [多语言覆盖](i18n.html)
 - [后台运行](background-running.html)
 - [hap 链接](hap-schema.html)
 - [页面启动模式](launch-mode.html)
 - [多屏适配](../../multi-screens/index.html)


---
## 文件: guide\framework\other\language-list.html

支持的语言列表如下：
 | `-`  | 中文  | 英语  | az-AZ  | 阿塞拜疆语  | Azerbaijani  | ms-MY  | 马来语  | Bahasa Melayu  | bs-BA  | 波斯尼亚语  | Bosnian  | ca-ES  | 加泰罗尼亚语  | Catalan  | cs-CZ  | 捷克语  | Czech  | da-DK  | 丹麦语  | Danish  | de-DE  | 德语  | German  | et-EE  | 爱沙尼亚语  | Estonian  | en-AU  | 英语（澳大利亚）  | English (Australia)  | en-IN  | 英语（印度）  | English (India)  | en-GB  | 英语（英国）  | English (United Kingdom)  | en-US  | 英语（美国）  | English (United States)  | es-ES  | 西班牙语（西班牙）  | Spanish (Spain)  | es-US  | 西班牙语（拉美）  | Spanish (Latin America)  | eu-ES  | 巴斯克语  | Basque  | fr-FR  | 法语  | French  | gl-ES  | 加利西亚语  | Galician  | ha-NG  | 豪萨语  | Hausa  | hr-HR  | 克罗地亚语  | Croatian  | in-ID  | 印尼语  | Indonesian  | it-IT  | 意大利语  | Italian  | lv-LV  | 拉脱维亚语  | Latvian  | lt-LT  | 立陶宛语  | Lithuanian  | hu-HU  | 匈牙利语  | Hungarian  | mt-MT  | 马耳他语  | Maltese  | nl-NL  | 荷兰语  | Dutch  | nb-NO  | 挪威语  | Norwegian  | uz-UZ  | 乌兹别克语  | Uzbek  | pl-PL  | 波兰语  | Polish  | pt-BR  | 葡萄牙语（巴西）  | Portuguese (Brazil)  | pt-PT  | 葡萄牙语（葡萄牙）  | Portuguese (Portugal)  | ro-RO  | 罗马尼亚语  | Romanian  | sq-AL  | 阿尔巴尼亚语  | Albanian  | sk-SK  | 斯洛伐克语  | Slovak  | sl-SI  | 斯洛文尼亚语  | Slovenian  | fi-FI  | 芬兰语  | Finnish  | sv-SE  | 瑞典语  | Swedish  | vi-VN  | 越南语  | Vietnamese  | tr-TR  | 土耳其语  | Turkish  | el-GR  | 希腊语  | Greek  | be-BY  | 白俄罗斯语  | Belarusian  | bg-BG  | 保加利亚语  | Bulgarian  | kk-KZ  | 哈萨克语  | Kazakh  | mk-MK  | 马其顿语  | Macedonian  | ru-RU  | 俄语  | Russian  | sr-RS  | 塞尔维亚语  | Serbian  | uk-UA  | 乌克兰语  | Ukrainian  | ka-GE  | 格鲁吉亚语  | Georgian  | hy-AM  | 亚美尼亚语  | Armenian  | iw-IL  | 希伯来语  | Hebrew  | ur-IN  | 乌尔都语（印度）  | Urdu (India)  | ur-PK  | 乌尔都语（巴基斯坦）  | Urdu (Pakistan)  | ar-EG  | 阿拉伯语  | Arabic  | fa-IR  | 波斯语  | Persian  | ne-IN  | 尼泊尔语（印度）  | Nepali (India)  | ne-NP  | 尼泊尔语（尼泊尔）  | Nepali (Nepal)  | mr-IN  | 马拉地语  | Marathi  | hi-IN  | 印地语  | Hindi  | as-IN  | 阿萨姆语  | Assamese  | bn-IN  | 孟加拉语 （印度）  | Bengali (India)  | bn-BD  | 孟加拉语 （孟加拉）  | Bengali (Bangladesh)  | pa-IN  | 旁遮普语  | Punjabi  | gu-IN  | 古吉拉特语  | Gujarati  | or-IN  | 奥里雅语  | Oriya  | ta-IN  | 泰米尔语  | Tamil  | te-IN  | 泰卢固语  | Telugu  | kn-IN  | 卡纳达语  | Kannada  | ml-IN  | 马拉雅拉姆语  | Malayalam  | th-TH  | 泰语  | Thai  | lo-LA  | 老挝语  | Lao  | my-MM  | 缅甸语  | Burmese  | km-KH  | 高棉 （柬埔寨语）  | Khmer  | ko-KR  | 韩语  | Korean  | ja-JP  | 日语  | Japanese  | zh-CN  | 中国（简体）  | Chinese (Simplified)  | zh-TW  | 中国（台湾繁体）  | Chinese (Taiwan Traditional)  | zh-HK  | 中国（香港繁体）  | Chinese (Hong Kong Traditional)  | sw-KE  | 斯瓦希里语  | Swahili


---
## 文件: guide\framework\other\launch-mode.html

# # 页面启动模式

# [#](launch-mode.html#页面启动模式) 页面启动模式
 用于定义页面的启动行为。
 ## [#](launch-mode.html#静态声明) 静态声明
 在 manifest 文件中页面路由信息 router.page 可增加启动模式字段 launchMode，用于声明该页面的启动模式。
 ### [#](launch-mode.html#页面启动模式参数) 页面启动模式参数：
 | 属性  | 类型  | 默认值  | 必填  | 描述  | launchMode  | String  | standard  | 否  | 声明页面的启动模式，支持"singleTask"，"standard"两种页面启动模式。
标识为"singleTask"模式时每次打开目标页面都会打开已有的目标页面并回调 onRefresh 生命周期函数，清除该页面上打开的其他页面，没有打开过此页面时会创建新的目标页面实例。
标识为"standard"模式时会每次打开新的目标页面（多次打开目标页面地址时会存在多个相同页面）  ### [#](launch-mode.html#示例) 示例：
 ```
"router": {
    "entry": "PageA",
    "pages": {
      "PageA": {
        "launchMode": "singleTask",
        "component": "index"
      },
      "PageB": {
        "launchMode": "standard",
        "component": "index"
      },
      "PageC": {
        "launchMode": "singleTask",
        "component": "index"
      }
    }
  }

```
打开页面的行为逻辑：
 若按顺序启动 PageA -> PageB -> PageC -> PageB -> PageC -> PageA
 
- 打开 PageA，首次打开时页面栈为空 页面栈为PageA
 - 打开 PageB，PageB 的启动模式为 standard，即在 PageA 之上新建 PageB 的页面实例并显示 页面栈为PageA,PageB
 - 打开 PageC，首次打开 PageC，即在 PageB 之上新建 PageC 的页面实例并显示 页面栈为PageA,PageB,PageC
 - 打开 PageB，PageB 的启动模式为 standard，即在 PageC 之上新建 PageB 的页面实例并显示 页面栈为PageA,PageB,PageC,PageB
 - 打开 PageC，PageC 页面实例已存在，即销毁 PageC 之上的页面实例 PageB，回到之前打开的 PageC 的页面实例并回调此页面生命周期的 onRefresh 函数 页面栈为PageA,PageB,PageC
 - 打开 PageA，PageA 页面实例已存在，即销毁 PageA 之上的页面实例 PageB 和 PageC，回到之前打开的 PageA 的页面实例并回调此页面生命周期的 onRefresh 函数 页面栈为PageA

 ## [#](launch-mode.html#动态声明) 动态声明
 动态声明有两种方式。一种是在 router.push 中携带启动标识参数，另一种是在打开页面的链接中携带启动标识参数。启动标识参数可以控制页面打开行为。
 ### [#](launch-mode.html#页面启动模式参数-2) 页面启动模式参数：
 | 参数  | 类型  | 必填  | 说明  | ___PARAM_LAUNCH_FLAG___  | String  | 否  | 跳转 JS 应用页面时传递的页面参数。携带 clearTask 时启动目标页面会清除此页面外的其他页面，存在多个目标页面时只保留最先打开的目标页面并回调 onRefresh 生命周期。如不存在目标页面时将清除所有页面并新建目标页面实例  ### [#](launch-mode.html#示例-2) 示例：
 ```
router.push({
  uri: '/PageB',
  params: {
    ___PARAM_LAUNCH_FLAG___: 'clearTask'
  }
})

```
打开页面的行为逻辑：
 若已经打开页面栈为 PageA -> PageB -> PageC，此时以 clearTask 标识启动 PageB
 
- 销毁 PageC 页面实例
 - 销毁 PageA 页面实例
 - PageB 页面实例已存在，回到此页面实例并回调此页面生命周期的 onRefresh 函数

 若已经打开页面栈为 PageA -> PageC，此时以 clearTask 标识启动 PageB
 
- 销毁 PageC 页面实例
 - 销毁 PageA 页面实例
 - PageB 页面实例不存在，新建 PageB 页面实例并显示


---
## 文件: guide\framework\script\global-data-method.html

# # 全局属性和方法

# [#](global-data-method.html#全局属性和方法) 全局属性和方法
 ## [#](global-data-method.html#对象) 对象
 ### [#](global-data-method.html#公共对象) 公共对象
 | 属性  | 类型  | 描述  | $app  | Object  | 应用对象  | $page  | Object  | 页面对象  | $valid  | Boolean  | 页面对象是否有效  #### [#](global-data-method.html#应用对象) 应用对象
 在页面中可通过 $app 访问到全局应用对象。
 在`app.ux`文件中，开发者可以定义全局可访问的数据和方法，在页面中通过`this.$app.$def`访问，在`app.ux`文件中，直接通过`this`访问。例如在`app.ux`文件中定义如下：
 ```

/**
 * 应用级别的配置，供所有页面公用
 */
export default {
  data: {
    a: 1
  },
  func: function() {
    console.log(this.data.a)
    console.log(`function executed!`)
  }
}

```
在其他页面可以这样调用：
 ```
this.$app.$def.data
this.$app.$def.func()

```
可通过 $app 访问如下内置方法：
 | 属性  | 类型  | 描述  | exit  | Function  | 退出 JS 应用，结束应用生命周期。调用方法：this.$app.exit()  #### [#](global-data-method.html#页面对象) 页面对象
 在页面中可通过this.$page访问到当前页面对象，该对象上可访问到如下属性：
 | 属性  | 类型  | 描述  | name  | String  | 获取当前页面路由的名称，与manifest 文件中router.pages 中对应的属性名一致  | path  | String  | 获取当前页面路由的 path，与manifest 文件中router.pages 中对应的 path 一致  | component  | String  | 获取当前页面路由的 component，与manifest 文件中router.pages 中对应的 component 一致  ## [#](global-data-method.html#方法) 方法
 ### [#](global-data-method.html#this-caniuse) this.$canIUse[3+](../../version/APILevel3.html)
 在页面中可通过 this.$canIUse 进行可使用的能力查询，包括接口和组件。
 #### [#](global-data-method.html#参数) 参数:
 | 类型  | 描述  | String  | 要查询的能力，格式见下方  #### [#](global-data-method.html#返回值) 返回值：
 | 类型  | 描述  | Boolean  | 查询的能力是否支持  #### [#](global-data-method.html#入参格式) 入参格式
 ##### [#](global-data-method.html#查询接口) 查询接口
 ```
// 查询feature下的方法是否支持
`@${featureName}.${method}`
// 查询某个feature是否支持
`@${featureName}`

```
**示例**
 ```
if (this.$canIUse('@system.router.push')) {
  // 可以使用方法@system.router.push
}
if (this.$canIUse('@system.router')) {
  // 可以使用@system.router接口
}

```
##### [#](global-data-method.html#查询组件) 查询组件
 type取值可以是`'attr'`、`'style'`、`'method'`，分别对应组件的属性、样式、方法。
 ```
// 查询组件下的属性、样式、方法是否支持
`${componentName}.${type}.${name}`
// 查询组件是否支持
`${componentName}`

```
**示例**
 ```
if (this.$canIUse('scroll')) {
  // 可以使用scroll组件
}
if (this.$canIUse('scroll.attr.scroll-x')) {
  // 可以使用scroll组件的scroll-x属性
}

```
### [#](global-data-method.html#this-watch) this.$watch
 监控数据改变。动态添加属性/事件绑定，属性必须在 data 中定义，handler 函数必须在``定义，当属性值发生变化时事件被触发。

如果是监听对象中的属性，参数请使用.分割，如：$watch(xxx.xxx.xxx, methodName)。
 #### [#](global-data-method.html#参数-2) 参数
 | 属性  | 类型  | 描述  | data  | String  | 属性名，支持'a.b.c'格式，不支持数组索引  | handler  | String  | 事件句柄函数名，函数的第一个参数为新属性值，第二个参数为旧的属性值  #### [#](global-data-method.html#代码示例) 代码示例
 ```

  export default {
    props: ['propObject'],
    data {
      say: '',
      propSay: ''
    },
    onInit() {
      // 监听数据变化
      this.$watch('say', 'watchDataChange')
      this.$watch('propObject.name', 'watchPropsChange')
    },
    /**
     * 监听数据变化，你可以对数据处理后，设置值到data上
     * @param newV
     * @param oldV
     */
    watchPropsChange(newV, oldV) {
      console.info(`监听数据变化：`, newV, oldV)
      this.propSay = newV && newV.toUpperCase()
    },
    watchDataChange(newV, oldV) {
      console.info(`监听数据变化：`, newV, oldV)
    }
  }

```
### [#](global-data-method.html#this-element) this.$element
 获取指定 id 的组件 dom 对象，如果没有指定 id，则返回根组件 dom 对象。
 #### [#](global-data-method.html#参数-3) 参数
 | 类型  | 描述  | String  | this.$element('idName')获取 dom 节点  #### [#](global-data-method.html#代码示例-2) 代码示例
 ```

  export default {
    onReady() {
      const el = this.$element('xxx')
      console.log(`输出xxx节点信息： ${el}`)
    }
  }

```
this.$element('xxx') 获取 id 为 xxx 的 div 组件实例对象， this.$element() 获取模板中的根组件实例对象。
 `id`属性赋值可以查看此[文档](../../../components/general/properties.html)。
 ### [#](global-data-method.html#this-nexttick) this.$nextTick
 在下次 DOM 更新循环结束之后执行延迟回调。在修改数据之后立即使用这个方法，可以获取更新后DOM。
 #### [#](global-data-method.html#参数-4) 参数
 | 类型  | 描述  | Function  | 回调函数中执行的是会对DOM进行操作的处理  #### [#](global-data-method.html#代码示例-3) 代码示例
 ```

    添加项目

        {{ $item }}

  export default {
    private: {
      list: ["项目1", "项目2"]
    },
    onAddClick() {
      this.list.push(Math.random())
      // 更新数据后,DOM没有立即发生变化。
      this.$element("list").getBoundingClientRect({
        success: (rect) => {
          console.log("getBoundingClientRect.height=", rect.height)
        }
      })
      this.$nextTick(() => {
        // 更新数据后,DOM发生变化。
        this.$element("list").getBoundingClientRect({
          success: (rect) => {
            console.log("$nextTick getBoundingClientRect.height=", rect.height)
          }
        })
      })
    }
  }

  .page {
    padding-top: 20px;
    width: 100%;
    height: 100%;
    flex-direction: column;
    justify-content: flex-start;
    align-items: center;
  }

  .list {
    width: 200px;
    flex-direction: column;
    align-items: center;
    border: 2px solid red;
  }

```
除了以上公共方法，还有this.$on、this.$off、this.$dispatch、this.$broadcast、this.$emit等事件方法用于父子组件通信。方法说明如下：
 | 方法  | 参数  | 描述  | this.$on  | type: String 事件名
handler: Function 事件句柄函数  | 添加事件处理句柄用法：this.$on('xxxx', this.fn)，fn 是在中定义的函数  | this.$off  | type: String 事件名
handler: Function 事件句柄函数  | 删除事件处理句柄用法：this.$off('xxxx', this.fn) this.$off('xxx') 删除指定事件的所有处理句柄  | this.$dispatch  | type: String 事件名  | 向上层组件发送事件通知用法：this.$dispatch('xxx')正常情况下，会一直向上传递事件（冒泡）如果要停止冒泡，在事件句柄函数中调用evt.stop()即可  | this.$broadcast  | type: String 事件名  | 向子组件发送事件通知用法：this.$broadcast('xxx')正常情况下，会一直向下传递事件如果要停止传递，在事件句柄函数中调用evt.stop()即可  | this.$emit  | type: String 事件名
data: Object 事件参数  | 触发事件，对应的句柄函数被调用用法：this.$emit('xxx') this.$emit('xxx', {a:1})传递的事件参数可在事件回调函数中，通过evt.detail来访问，例如evt.detail.a  事件方法使用示例可参考[文档](../template/component.html#父子组件通信)。


---
## 文件: guide\framework\script\index.html

# # script 脚本

# [#](index.html#script-脚本) script 脚本
 ux 文件中用来定义页面的逻辑代码，包括数据对象、生命周期接口、自定义方法、通用方法等。
 ## [#](index.html#语法) 语法
 支持 ES5 / ES6 语法
 ### [#](index.html#模块声明) 模块声明
 可以通过 import 引入功能模块，在代码中调用模块方法（具体参看接口部分文档说明）：
 ```
import fetch from '@system.fetch'

```
### [#](index.html#代码引用) 代码引用
 JS 代码引用推荐使用`import`来导入，例如：
 ```
import utils from '../common/utils.js'

```
注意
 JS 应用环境不是 node 环境，不要引用 node 原生模块，如 `import fs from 'fs'`。

 ## [#](index.html#页面数据对象) 页面数据对象
 页面文件中可以定义页面级的数据对象，用于在模板中绑定和在页面方法中操作。例如：
 ```

    {{a}}

  export default {
    // 页面的数据对象，影响传入数据的覆盖机制：private内定义的属性不允许被覆盖
    private: {
      a: 1
    }
  }

```
其他详细说明参见[页面数据对象](page-data.html)。
 ## [#](index.html#生命周期接口) 生命周期接口
 应用和页面都有预定义的一系列生命周期，可在脚本中声明若干生命周期钩子函数，在执行到该特定生命周期阶段时，会调用这些钩子函数，例如：
 ```
// 会在应用/页面被初始化的时候执行这个函数
onInit() {
  console.log('page initialized！')
}

```
其他详细说明参见[生命周期](lifecycle.html)。
 ## [#](index.html#自定义方法) 自定义方法
 开发者可以在应用/页面 ux 文件中按需声明自定义方法，包括一些工具方法、事件回调方法等，例如：
 ```
onBtnClick(){
  console.log('button clicked!')
}

```
## [#](index.html#全局对象和方法) 全局对象和方法
 开发者在应用ux文件(`app.ux`)中声明的属性和方法，可以在任意页面中通过`this.$app.$def`进行方法，详情参考[全局对象和方法](global-data-method.html)。
 ## [#](index.html#通用方法) 通用方法
 框架提供了一些预定义的通用方法供页面/组件调用，详情参考[通用方法](../../../components/general/methods.html)。


---
## 文件: guide\framework\script\lifecycle.html

# # 生命周期

# [#](lifecycle.html#生命周期) 生命周期
 
- [页面的生命周期](lifecycle.html#%E9%A1%B5%E9%9D%A2%E7%9A%84%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F)：`onInit`、`onReady`、`onShow`、`onHide`、`onDestroy`、`onBackPress`、`onRefresh`、`onConfigurationChanged`
 - 页面的状态：`显示`、`隐藏`、`销毁`
 - [APP 的生命周期](lifecycle.html#app%E7%9A%84%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F)：`onCreate`、`onShow`、`onHide`、`onDestroy` 、`onError`

 ## [#](lifecycle.html#生命周期图) 生命周期图
 ![生命周期图](../../../../images/components/life.png)
 ## [#](lifecycle.html#页面的生命周期) 页面的生命周期
 由于页面通过`ViewModel`渲染，那么页面的生命周期指的也就是`ViewModel`的生命周期，包括常见的：onInit, onReady, onShow 在**页面创建**时触发调用。
 ### [#](lifecycle.html#oninit) onInit()
 **表示`ViewModel`的数据已经准备好**，可以开始使用页面中的数据。
 **示例如下：**
 ```
private: {
  // 生命周期的文本列表
  lcList: []
},
onInit () {
  this.lcList.push('onInit')

  console.info(`触发：onInit`)
  // 执行：获取ViewModel的lcList属性：onInit
  console.info(`执行：获取ViewModel的lcList属性：${this.lcList}`)
  // $app信息
  console.info(`获取：manifest.json的config.data的数据：${this.$app.$data.name}`)
  console.info(`获取：APP文件中的数据：${this.$app.$def.data1.name}`)
  console.info(`执行：APP文件中的方法`, this.$app.$def.method1())
}

```
### [#](lifecycle.html#onready) onReady()
 **表示`ViewModel`的模板已经编译完成**，可以开始获取 DOM 节点（如：`this.$element(idxxx)`）。
 **示例如下：**
 ```
onReady () {
  this.lcList.push('onReady')
  console.info(`触发：onReady`)
}

```
### [#](lifecycle.html#onshow-onhide) onShow(), onHide()
 APP 中可以同时运行多个页面，但是**每次只能显示其中一个页面**；这点不同于纯前端开发，浏览器页面中每次只能有一个页面，当前页面打开另一个页面，上个页面就销毁了；不过和 SPA 开发有点相似，切换页面但浏览器全局 Context 是共享的。
 所以页面的切换，就产生了新的事件：页面被切换隐藏时调用 onHide()，页面被切换重新显示时调用 onShow()。
 **示例如下：**
 ```
onShow () {
  this.lcList.push('onShow')
  console.info(`触发：onShow`)
},
onHide () {
  this.lcList.push('onHide')
  console.info(`触发：onHide`)
}

```
### [#](lifecycle.html#ondestroy) onDestroy()
 页面被销毁时调用，被销毁的可能原因有：用户从当前页面返回到上一页，或者用户打开了太多的页面，框架自动销毁掉部分页面，避免占用资源。
 所以，页面销毁时应该做一些**释放资源**的操作，如：取消接口订阅监听`geolocation.unsubscribe()`。
 判断页面是否处于被销毁状态，可以调用 `ViewModel` 的 `$valid` 属性：`true` 表示存在，`false` 表示销毁。
 **示例如下：**
 ```
onDestroy () {
  console.info(`触发：onDestroy`)
  console.info(`执行：页面要被销毁，销毁状态：${this.$valid}，应该做取消接口订阅监听的操作: geolocation.unsubscribe() `) // true，即将销毁
  setTimeout(function () {
    // 页面已销毁，不会执行
    console.info(`执行：页面已被销毁，不会执行`)
  }.bind(this), 0)
}

```
**提示：**
 
- `setTimeout`之类的异步操作绑定在了当前页面上，因此当页面销毁之后异步调用不会执行。

 ### [#](lifecycle.html#onbackpress) onBackPress()
 当用户`右滑返回`或点击`返回实体按键`时触发该事件。
 如果事件响应方法最后返回`true`表示不返回，自己处理业务逻辑（完毕后开发者自行调用 API 返回）；否则：不返回数据，或者返回其它数据，表示遵循系统逻辑：返回到上一页。
 **示例如下：**
 ```
onBackPress () {
  console.info(`触发：onBackPress`)
  // true：表示自己处理；否则默认返回上一页
  // return true
}

```
### [#](lifecycle.html#onrefresh-query) onRefresh(query)
 监听页面重新打开。
 1.当页面在 manifest 中 launchMode 标识为'singleTask'时，仅会存在一个目标页面实例，用户多次打开目标页面时触发此函数。

2.打开目标页面时在 push 参数中携带 flag 'clearTask'，且页面实例已经存在时触发。该回调中参数为重新打开该页面时携带的参数，详见[页面启动模式](../other/launch-mode.html)。
 **示例如下：**
 ```
onRefresh(query) {
  // launchMode 为 singleTask 时，重新打开页面时携带的参数不会自动更新到页面 this 对象上
  // 需要在此处从 query 中拿到并手动更新
  console.log('page refreshed!!!')
}

```
### [#](lifecycle.html#onconfigurationchanged-event) onConfigurationChanged(event)
 监听应用配置发生变化。当应用配置发生变化时触发，如系统语言改变。
 **参数**
 | 参数名  | 类型  | 描述  | event  | Object  | 应用配置发生变化的事件  **event参数**
 | 参数名  | 类型  | 描述  | type  | String  | 应用配置发生变化的原因类型，支持的 type 值如下所示  **event 中 type 现在支持的参数值如下**
 | 参数名  | 描述  | locale  | 应用配置因为语言、地区变化而发生改变  **示例如下：**
 ```
onConfigurationChanged(evt) {
  console.log(`触发生命周期onConfigurationChanged, 配置类型：${evt.type}`)
}

```
## [#](lifecycle.html#app的生命周期) APP的生命周期
 当前为 APP 的生命周期提供了五个回调函数：onCreate()、onShow()、onHide()、onDestroy()、onError(e)。
 **示例如下：**
 ```
export default {
  // 监听应用创建,应用创建时调用
  onCreate() { 
    console.info('Application onCreate')
  },
  // 监听应用返回前台,应用返回前台时调用
  onShow() { 
    console.info('Application onShow')
  },
  // 监听应用退到后台,应用退到后台时调用
  onHide() { 
    console.info('Application onHide')
  },
  // 监听应用销毁,应用销毁时调用
  onDestroy() { 
    console.info('Application onDesteroy')
  },
  // 监听应用报错,应用捕获异常时调用,参数为Error对象。
  onError(e) {
    console.log('Application onError', e)
  },
  // 暴露给所有页面，在页面中通过：this.$app.$def.method1()访问
  method1() {
    console.info('这是APP的方法')
  },
  // 暴露给所有页面，在页面中通过：this.$app.$def.data1访问
  data1: {
    name: '这是APP存的数据'
  }
}

```


---
## 文件: guide\framework\script\page-data.html

# # 页面数据对象

# [#](page-data.html#页面数据对象) 页面数据对象
 | 属性  | 类型  | 描述  | data  | Object  | 组件级的数据模型，属性名不能以$或_开头，不要使用 for, if, show, tid 等保留字  | public  | Object  | 页面级组件的数据模型，影响传入数据的覆盖机制：public 内定义的属性允许被传入的数据覆盖，如果外部传入数据的某个属性未被声明，在 public 中不会新增这个属性  | protected  | Object  | 页面级组件的数据模型，影响传入数据的覆盖机制：protected 内定义的属性，允许被应用内部页面请求传递的数据覆盖，不允许被应用外部请求传递的数据覆盖  | private  | Object  | 页面级组件的数据模型，影响传入数据的覆盖机制：private 内定义的属性不允许被覆盖  | computed  | Object  | 计算属性，属性名不能以$或_开头, 不要使用 for, if, show, tid 等保留字  温馨提示
 **注意 public，protected，private 不能与 data 同时使用。**


---
## 文件: guide\framework\style\index.html

# # style 样式

# [#](index.html#style-样式) style 样式
 用于描述 template 模板的组件样式，决定组件应该如何显示。
 样式布局采用 CSS Flexbox（弹性盒）样式，针对部分原生组件，对 CSS 进行了少量的扩充以及修改。
 为了解决屏幕适配问题，所有与大小相关的样式（例如 width、font-size）均以基准宽度（默认 480px）为基础，根据实际屏幕宽度进行缩放，例如 width:100px，在 960px 宽度屏幕上，width 实际上为 200px。
 ## [#](index.html#文件导入) 文件导入
 支持 2 种导入外部文件的方式：
 ```

  @import './style.css';
  .a {
  }

```
## [#](index.html#模板内部样式) 模板内部样式
 支持使用 style、class 属性来控制组件的样式：
 ```

```
## [#](index.html#选择器) 选择器
 支持的选择器有：
 | 选择器  | 样例  | 样例描述  | .class  | .intro  | 选择所有拥有 class="intro" 的组件  | #id  | #firstname  | 选择拥有 id="firstname" 的组件  | tag  | div  | 选择所有 div 组件  | ,  | .a, .b  | 选择所有 class="a"以及 class="b"的组件  ```

  /* 单个选择器 */
  text {
  }
  .class-abc {
  }
  #idAbc {
  }
  /* 同一样式适应多个选择器 */
  .font-text, .font-comma {
  }

```
 ## [#](index.html#选择器优先级) 选择器优先级
 当前样式的选择器的优先级计算保持与浏览器一致，是浏览器 CSS 渲染的一个子集（仅支持：inline, id, class, tag）。
 多条 CSS 声明可以匹配到同一个元素 如 div，应用在该元素上的 CSS 声明总体优先级是：inline > #id > .class > tag，这四大类匹配到该元素的多个 CSS 声明，如：``，其优先级按照各选择器的权值高低之和来比较。选择器的优先级权值如下：
 
- `ID选择器`（例如: #hello）的权值为 10000

 - `类选择器`（例如: .example）的权值为 100

 - `类型选择器`（例如: h1）的权值为 1

 css 的优先级计算文档也可以查看[MDN 文档  (opens new window)](https://developer.mozilla.org/zh-CN/docs/Web/CSS/Specificity)入门
 ## [#](index.html#样式预编译) 样式预编译
 目前 JS 应用支持`less`与`sass`的预编译，具体教程也可以参考[这里](page-style-and-layout.html#引入-less-scss-预编译)。
 ```

  @import './lessFile.less';
  .less-font-text, .less-font-comma {
    font-size: 60px;
  }

```


---
## 文件: guide\framework\style\media-query.html

# # 媒体查询2+

# [#](media-query.html#媒体查询) 媒体查询[2+](../../version/APILevel2.html)
 通过媒体查询(media query)，开发者可以根据各种设备特征和参数的值或者是否存在来调整JS 应用的样式。
 媒体查询是响应式设计的一部分。和 css 类似，可使用 @media at-rule 根据媒体查询的结果，有条件地应用样式表的一部分；也可使用 @import 有条件地应用整个样式表。
 aiot-toolkit最低版本：1.1.3
 ## [#](media-query.html#语法) 语法
 每条媒体查询语句都由一个可选的媒体类型和任意数量的媒体特性表达式构成，可以使用多种逻辑操作符合并多条媒体查询语句，媒体查询语句不区分大小写。
 有两种方法可以执行媒体查询：
 ### [#](media-query.html#media-方式引入媒体查询) @media 方式引入媒体查询
 ```
@media [media type] [and|not|only] [(media feature)] {
  CSS-Code;
}

```
### [#](media-query.html#举例) 举例
 
- @media (max-width: 30) { ... } // level3的写法。
 - @media (width <= 30) { ... } // level4的写法，比level3更清晰简洁。
 - @media screen and (min-width: 400) and (max-width: 700) { ... } // 多条件写法。
 - @media (400 <= width <= 700) { ... } // 多条件level4写法。

 ### [#](media-query.html#import-方式引入媒体查询) @import 方式引入媒体查询[3+](../../version/APILevel3.html)
 ```
@import './css_file_name.css' [media type] [and|not|only] [(media feature) ];

```
## [#](media-query.html#媒体类型) 媒体类型
 媒体类型（Media types）描述设备的类别。除了在使用 not 或 only 逻辑操作符必须一并填上媒体类型；其他时候，媒体类型是可选择是否填入的。目前JS 应用支持的媒体类型如下：
 | 媒体类型  | 简介  | screen  | 主要用于屏幕。  ## [#](media-query.html#媒体特性) 媒体特性
 媒体特性表达式是完全可选的，它负责测试这些特性或特征是否存在、值为多少。
 每条媒体特性表达式都必须用括号括起来。
 目前JS 应用支持的媒体特性如下：
 | 类型  | 描述  | 查询时是否需带单位  | 支持单位  | height[3+](../../version/APILevel3.html)  | 定义输出设备中的页面可视区域高度  | 否  | dp  | min-height[3+](../../version/APILevel3.html)  | 定义输出设备中的页面可视区域最小高度  | 否  | dp  | max-height[3+](../../version/APILevel3.html)  | 定义输出设备中的页面可视区域最大高度  | 否  | dp  | width[3+](../../version/APILevel3.html)  | 定义输出设备中的页面可视区域宽度  | 否  | dp  | min-width[3+](../../version/APILevel3.html)  | 定义输出设备中的页面可视区域最小宽度  | 否  | dp  | max-width[3+](../../version/APILevel3.html)  | 定义输出设备中的页面可视区域最大宽度  | 否  | dp  | aspect-ratio[3+](../../version/APILevel3.html)  | 定义输出设备中的页面可见区域宽高比，比例值需要按照 x / y 的格式，例如 1 / 2  | 否  | 无  | min-aspect-ratio[3+](../../version/APILevel3.html)  | 定义输出设备中的页面可见区域最小宽高比，参数要求同上  | 否  | 无  | max-aspect-ratio[3+](../../version/APILevel3.html)  | 定义输出设备中的页面可见区域最大宽高比，参数要求同上  | 否  | 无  | device-type[3+](../../version/APILevel3.html)  | device-type 的可选值为：watch、band、smartspeaker，默认值：watch  | 否  | 无  | shape[2+](../../version/APILevel2.html)  | 屏幕形状，可选值：circle、rect、pill-shaped[3+](../../version/APILevel3.html)  | 否  | 无  ### [#](media-query.html#注意) 注意
 1.在媒体特性列表中，标记了“查询时不带单位”的媒体特性，如 width、height 的查询，都不带长度单位，且长度单位只能为dp
 dp 数值 = 物理分辨率 / 设备像素比(device pixel ratio)
 举例：一个设备分辨率为 480*480，设备像素比 = 2，屏幕宽度 = 480 像素 = 240dp
 各设备数据参考
 | 设备类型  | 设备型号  | 屏幕形状  | 屏幕尺寸  | 分辨率  | PPI  | DPR  | 水平DP值  | 手表  | Xiaomi Watch S1 Pro  | 圆形  | 1.47英寸  | 480x480  | 326  | 2.0  | 240  | 手表  | Xiaomi Watch H1  | 圆形  | 1.43英寸  | 466x466  | 326  | 2.0  | 233  | 手表  | Xiaomi Watch S3  | 圆形  | 1.43英寸  | 466x466  | 326  | 2.0  | 233  | 手表  | Xiaomi Watch S4 sport  | 圆形  | 1.43英寸  | 466x466  | 326  | 2.0  | 233  | 手表  | Xiaomi Watch S4  | 圆形  | 1.43英寸  | 466x466  | 326  | 2.0  | 233  | 手表  | REDMI Watch 5  | 矩形  | 2.07英寸  | 432x514  | 324  | 2.0  | 216  | 手环  | 小米手环8 Pro  | 矩形  | 1.74英寸  | 336x480  | 336  | 2.1  | 168  | 手环  | 小米手环9  | 胶囊形  | 1.62英寸  | 192x490  | 325  | 2.0  | 96  | 手环  | 小米手环9 Pro  | 矩形  | 1.74英寸  | 336x480  | 336  | 2.1  | 168  | 手环  | 小米手环10  | 胶囊形  | 1.725英寸  | 212x520  | 326  | 2.0  | 106  示例代码：
 ```
//以下media query会在屏幕宽度为80dp ~ 160dp范围设备上生效
@media (min-width: 80) and (max-width: 160) {
  .box {
    background-color: green;
  }
}

//以下media query会在屏幕宽度为160dp ~ 200dp范围设备上生效
@media (min-width: 160) and (max-width: 200) {
  .box {
    background-color: yellow;
  }
}

//以下media query会在屏幕宽度为200dp ~ 300dp范围设备上生效
@media (min-width: 200) and (max-width: 300) {
  .box {
    background-color: red;
  }
}

```
## [#](media-query.html#逻辑操作符) 逻辑操作符[3+](../../version/APILevel3.html)
 开发者可以使用逻辑操作符组合多个媒体特性的查询条件，编写复杂的媒体查询。
 | 类型  | 描述  | and  | and 运算符用于将多个媒体特性组合到一个单独的媒体查询中，要求每个链接的特性返回 true，则此时查询为真  | not  | not 运算符用于否定媒体查询，如果查询不返回 false，则返回 true。如果出现在逗号分隔的列表中，它只会否定应用它的特定查询。如果使用 not 运算符，则必须指定显式媒体类型。例如：not screen and (min-width: 400) and (max-width: 700)注：not 关键字不能用于否定单个功能表达式，它会作用于整个媒体查询  | only  | only 运算符仅用于整个查询匹配应用样式，手表应用处理以 only 开头的关键词时将会忽略 only。如果使用 only 运算符，必须指定媒体类型。例如：only screen and (min-width: 400) and (max-width: 700)  | ,(逗号)  | 逗号分隔效果等同于 or 逻辑操作符。当使用逗号分隔的媒体查询时，如果任何一个媒体查询返回真，样式就是有效的。例如：(width >= 192), (height >= 490)  | or  | or 运算符用于将多个媒体特性比较语句组合到一个媒体查询语句中，只要有其中一条媒体特性比较语句返回 true，查询成立。例如：(min-width: 400) or (max-width: 700)  | =  | 大于等于。例如： (500 >= height)  |   | 大于。例如： (500 > height)  ## [#](media-query.html#示例代码) 示例代码
 
- 查询形状为圆形或胶囊形
 ```
  .box {
    width: 100px;
    height: 100px;
    background-color: black;
  }

  @media (shape: circle) or (shape: pill-shaped) {
    .box {
      background-color: green;
    }
  }

```

 - 同时查询设备类型为手表，屏幕形状为圆形
 ```
  .box {
    width: 100px;
    height: 100px;
    background-color: black;
  }

  @media (device-type: watch) and (shape: circle) {
    .box {
      background-color: green;
    }
  }

```

 ## [#](media-query.html#支持明细) 支持明细
 | 设备产品  | 说明  | 小米 S1 Pro 运动健康手表  | 不支持  | 小米手环 8 Pro  | 不支持  | 小米手环 9 / 9 Pro  | 支持2+特性  | Xiaomi Watch S3  | 支持2+特性  | Redmi Watch 4  | 不支持  | 小米腕部心电血压记录仪  | 不支持  | 小米手环 10  | 支持  | Xiaomi Watch S4  | 支持  | REDMI Watch 5  | 支持  | REDMI Watch 6  | 支持


---
## 文件: guide\framework\style\page-style-and-layout.html

# # 页面样式与布局

# [#](page-style-and-layout.html#页面样式与布局) 页面样式与布局
 ## [#](page-style-and-layout.html#盒模型) 盒模型
 JS 应用布局框架使用 border-box 模型，具体表现与宽高边距计算可参考 MDN 文档[box-sizing  (opens new window)](https://developer.mozilla.org/zh-CN/docs/Web/CSS/box-sizing)，暂不支持 content-box 模型与手动指定 box-sizing 属性。
 ![](../../../../images/components/border-box.png)
 布局所占宽度 Width：
 `Width = width(包含padding-left + padding-right + border-left + border-right)`
 布局所占高度 Height：
 `Height = height(包含padding-top + padding-bottom + border-top + border-bottom)`
 ## [#](page-style-and-layout.html#长度单位) 长度单位
 框架对长度单位的支持，支持长度单位`px`、`%`、`dp`。
 ### [#](page-style-and-layout.html#px) px
 与传统 web 页面不同，`px`是相对于`项目配置基准宽度`的单位，已经适配了移动端屏幕，其原理类似于`rem`。
 开发者只需按照设计稿确定框架样式中的 px 值即可。
 首先，我们需要定义`项目配置基准宽度`，它是项目的配置文件（`/src/manifest.json`）中`config.designWidth`的值，默认不填则为 480。
 然后， `设计稿1px`与`框架样式1px`转换公式如下：
 ```
设计稿1px / 设计稿基准宽度 = 框架样式1px / 项目配置基准宽度

```
**示例如下：**
 若设计稿宽度为 640px，元素 A 在设计稿上的宽度为 100px，实现的两种方案如下：
 **方案一：**
 修改`项目配置基准宽度`：将`项目配置基准宽度`设置为`设计稿基准宽度`，则`框架样式1px`等于`设计稿1px`
 
- 设置`项目配置基准宽度`，在项目的配置文件（`/src/manifest.json`）中，修改`config.designWidth`：

 ```
{
  "config": {
    "designWidth": 640
  }
}

```

- 设置元素 A 对应的框架样式：

 ```
width: 100px;

```
**方案二：**
 不修改`项目配置基准宽度`：若当前项目配置的`项目配置基准宽度`为 480，设元素 A 的框架样式 x`px`，由转换公式得：`100 / 640 = x / 480`。
 
- 设置元素 A 对应的框架样式：

 ```
width: 75px;

```
### [#](page-style-and-layout.html#百分比) 百分比%
 JS 应用的百分比计算规则与 css 类似，可参考[MDN 文档  (opens new window)](https://developer.mozilla.org/zh-CN/docs/Web/CSS/percentage)。
 ### [#](page-style-and-layout.html#dp) dp[3+](../../version/APILevel3.html)
 dp 单位，全称为 device independent pixels，即设备独立像素。
 计算公式：dp 数值 = 物理分辨率 / 设备像素比(device pixel ratio)
 举例：一个设备分辨率为 480*480，设备像素比 = 2，屏幕宽度 = 480 像素 = 240dp
 示例代码：
 ```

  .dp-box{
    width:360dp;
    height:360dp;
    background-color:green;
    margin-bottom:40px;
  }

```
## [#](page-style-and-layout.html#设置定位) 设置定位
 position 支持2种属性值：relative、absolute，并且默认值为 relative，可以参考[MDN 文档  (opens new window)](https://developer.mozilla.org/zh-CN/docs/Web/CSS/position)。
 ## [#](page-style-and-layout.html#设置样式) 设置样式
 开发者可以使用`内联样式`、`tag选择器`、`class选择器`、`id选择器`来为组件设置样式
 同时也可以使用`并列选择`设置样式，暂时不支持`后代选择器`。
 详细的文档可以查看[此处](index.html)。
 **示例如下：**
 ```

    内联样式
    ID选择器
    class选择器
    tag选择器

  .page {
    flex-direction: column;
  }
  /* tag选择器 */
  text {
    color: #0000FF;
  }
  /* class选择器（推荐） */
  .title {
    color: #00FF00;
  }
  /* ID选择器 */
  #title {
    color: #00A000;
  }
  /* 并列选择 */
  .title, #title {
    font-weight: bold;
  }

```
## [#](page-style-and-layout.html#通用样式) 通用样式
 通用样式如 margin，padding 等属性可以点击[此处](../../../components/general/style.html)查询。
 ## [#](page-style-and-layout.html#flex-布局示例) Flex 布局示例
 框架使用`Flex布局`，关于`Flex布局`可以参考外部文档[A Complete Guide to Flexbox  (opens new window)](https://css-tricks.com/snippets/css/a-guide-to-flexbox/)。
 `Flex布局`的支持也可以在官网文档的[通用样式](../../../components/general/style.html)查询。
 div 组件为最常用的 Flex 容器组件，具有 Flex 布局的特性；text、span组件为文本容器组件，**其它组件不能直接放置文本内容**。
 **示例如下：**
 ```

      item1

      item2

  .page {
    /* 交叉轴居中 */
    align-items: center;
    /* 纵向排列 */
    flex-direction: column;
  }
  .item {
    /* 有剩余空间时，允许被拉伸 */
    /*flex-grow: 1;*/
    /* 空间不够用时，不允许被压缩 */
    flex-shrink: 0;
    /* 主轴居中 */
    justify-content: center;
    width: 200px;
    height: 100px;
    margin: 10px;
    background-color: #FF0000;
  }

```
## [#](page-style-and-layout.html#动态修改样式) 动态修改样式
 动态修改样式有多种方式，与传统前端开发习惯一致，包括但不限于以下：
 
- **修改 class**：更新组件的 class 属性中使用的变量的值
 - **修改内联 style**：更新组件的 style 属性中的某个 CSS 的值
 - **修改绑定的对象**：通过绑定的对象控制元素的样式

 **示例如下：**
 ```

    点击我修改文字颜色
    
    点击我修改文字颜色
    
    点击我修改文字颜色

  .normal-text {
    font-weight: bold;
  }
  .text-blue {
    color: #0faeff;
  }
  .text-red {
    color: #f76160;
  }

  export default {
    private: {
      className: 'text-blue',
      textColor: '#0faeff',
      styleObj: {
        color: 'red'
      }
    },
    onInit () {
      console.info('动态修改样式')
    },
    changeClassName () {
      this.className = 'text-red'
    },
    changeInlineStyle () {
      this.textColor = '#f76160'
    },
    changeStyleObj () {
      this.styleObj = {
        color: 'yellow'
      }
    }
  }

```
## [#](page-style-and-layout.html#引入-less-scss-预编译) 引入 less/scss 预编译
 ### [#](page-style-and-layout.html#less-篇) less 篇
 less 语法入门请参考[less 中文官网  (opens new window)](https://less.bootcss.com/)。
 使用 less 请先安装相应的类库：`less`、`less-loader`：
 ```
npm i less less-loader

```
详见文档[样式语法 --> 样式预编译](index.html#样式预编译)；然后在``标签上添加属性`lang="less"` **示例如下：**
 ```

    less示例!

  /* 引入外部less文件 */
  @import './style.less';
  /* 使用less */

```
### [#](page-style-and-layout.html#scss-篇) scss 篇
 scss 语法入门请参考[scss 中文官网  (opens new window)](https://www.sasscss.com/)。
 使用 scss 请在JS 应用项目下执行以下命令安装相应的类库：`node-sass`、`sass-loader`：
 ```
npm i node-sass sass-loader

```
详见文档[style 样式 --> 样式预编译](index.html#样式预编译)；然后在``标签上添加属性`lang="scss"`。
**示例如下：**
 ```

    less示例!

  /* 引入外部scss文件 */
  @import './style.scss';
  /* 使用scss */

```
## [#](page-style-and-layout.html#使用-postcss-解析-css) 使用 postcss 解析 css
 JS 应用支持 postcss 来解析 css，postcss 可以采用类似 less，sass 的语法来解析 css 了，比如支持变量，嵌套，定义函数等功能了。
 使用 postcss 解析 css 分为 3 个步骤：
 1.安装对应的 loader：
 > npm i postcss-loader precss@3.1.2 -D
>
 2.在项目的根目录新建一个 postcss.config.js，增加如下内容：
 ```
module.exports = {
  plugins: [require('precss')]
}

```
其中 precss 为 postcss 的插件。
 3.在页面对应的 style 标签上增加 lang="postcss"，如下：
 ```

  /* 使用postcss */
  .page {
    justify-content: center;
    background-color: #00beaf;
  }
  
  #title {
    color: #FF0000;
  }

```
这样就可以在 css 里面书写对应的代码了。
 说明
 如果想支持更多的语法格式，可以在 postcss.config.js 文件里面添加更多的插件，关于 postcss 的插件见[插件地址  (opens new window)](https://github.com/postcss/postcss/blob/master/docs/plugins.md)。


---
## 文件: guide\framework\template\component.html

# # 组件

# [#](component.html#组件) 组件
 ## [#](component.html#组件自定义) 组件自定义
 开发页面时开发者必须用到 Native 组件，如：`text`、`div`，这些组件是由各平台 Native 底层渲染出来的；如果开发一个复杂的页面，开发者把所有的 UI 部分写在一个文件的``，那代码的可维护性将会很低，并且模块之间容易产生不必要的耦合关系。
 为了更好的组织逻辑与代码，可以把页面按照功能拆成多个模块，每个模块负责其中的一个功能部分，最后页面将这些模块引入管理起来，传递业务与配置数据完成代码分离，那么这就是自定义组件的意义。
 自定义组件是一个开发者编写的组件，使用起来和 Native 一样，最终按照组件的``来渲染；同时开发起来又和页面一样，拥有 ViewModel 实现对数据、事件、方法的管理。
 提示
 由于自定义组件拥有独立的ViewModel，因此存在一定内存开销，在手表手环等轻量级设备上不建议使用。

 **示例如下：**
 ```

    自定义组件:
    {{ say }}
    {{ obj.name }}

  .tutorial-page {
    flex-direction: column;
    padding-top: 20px;

    .tutorial-title {
      font-weight: bold;
    }
  }

  // 子组件
  export default {
    data: {
      say: 'hello',
      obj: {
        name: 'quickApp'
      }
    },
    onInit() {
      console.log('我是子组件')
    }
  }

```
自定义组件中数据模型只能使用**data 属性** ，data 类型是 ** Object **。
 ### [#](component.html#自定义组件生命周期) 自定义组件生命周期：
 `onInit` ：表示组件ViewModel的数据已经准备好，可以开始使用页面中的数据。
 `onReady` ：表示组件ViewModel的模板已经编译完成，可以开始获取 DOM 节点。
 `onDestroy` ：组件被销毁时调用，组件销毁时应该做一些释放资源的操作，例如释放定时器等。
 ## [#](component.html#组件引入) 组件引入
 vela中是通过``标签引入组件，如下面代码所示：
 ```

```
``标签中的`src`属性指定自定义组件的地址，`name`属性指定在父组件中引用该组件时使用的 **标签名称**。
 **示例如下：**
 ```

    引入组件：

  .tutorial-page {
    flex-direction: column;
    padding: 20px 10px;
  }
  .tutorial-title {
      font-weight: bold;
  }

  // 父组件
  export default {
    private: {},
    onInit() {
      console.log('引入组件')
    }
  }

```
## [#](component.html#父子组件通信) 父子组件通信
 ### [#](component.html#父组件通过-prop-向子组件传递数据) 父组件通过 Prop 向子组件传递数据
 父组件向子组件传递数据，通过在子组件的`props`属性中声明对外暴露的属性名称，然后在组件引用标签上声明传递的父组件数据，详见[Props](Props.html)部分。
 **示例如下：**
 ```

    子组件:
    {{ say }}
    {{ propObject.name }}

  export default {
    props: ['say', 'propObject'],
    onInit() {
      console.info(`外部传递的数据：`, this.say, this.propObject)
    }
  }

```
```

  export default {
    private: {
      say:'hello'
      obj:{
        name:'child-demo'
      }
    }
  }

```
### [#](component.html#子组件对父组件通信) 子组件对父组件通信
 
- 子组件通过`$emit()`触发在节点上绑定的自定义事件来执行父组件的方法，如父组件与组件一；
 - 子组件通过`$dispatch()`触发自定义事件，父组件通过`$on()`监控自定义事件的触发，如父组件与组件二；

 **示例如下：**
 ```

    我是父组件count:{{count}}

    我是父组件num:{{num}}

    我是父组件age:{{age}}

  export default {
    private:{
      count:20,
      num:20,
      age:18
    },
    onInit(){
      this.$on('dispatchEvt',this.dispatchEvt)
    },
    emitEvt(evt){
      this.count = evt.detail.count
    },
    dispatchEvt(evt){
      this.num = evt.detail.num
    },
    evtTypeEmit(){
      this.$broadcast('broadevt',{
        age:19
      })
    },
  }

```
```

    我是子组件一count:{{compCount}}

  export default {
    props: ['count'],
    data(){
      return{
        compCount:this.count
      }
    },
    addHandler(){
      this.compCount ++
      this.$emit('emitEvt',{
        count:this.compCount
      })
    },
  }

```
```

    我是子组件二num:{{compNum}}

  export default {
    props: ['num'],
    data(){
      return{
        compNum:this.num
      }
    },
    delHandler(){
      this.compNum --
      this.$dispatch('dispatchEvt',{
        num:this.compNum
      })
    },
  }

```
```

    我是子组件三age:{{compAge}}

  export default {
    props:[],
    data(){
      return{
        compAge:null
      }
    },
    onInit(){
      this.$on('broadevt',this.broadevt)
    },
    broadevt(evt){
      this.compAge = evt.detail.age
    }
  }

```
框架向开发者提供了双向的事件传递
 
- 向下传递：父组件触发，子组件响应；调用`parentVm.$broadcast()`完成向下传递，如：broadevt
 - 向上传递：子组件触发，父组件响应；调用`childVm.$dispatch()`完成向上传递，如：dispatchEvt

 **提示：**
 
- 触发时传递参数，再接收时使用`evt.detail`来获取参数
 - 当传递结束后，可以调用`evt.stop()`来结束传递,否则会一直传递下去


---
## 文件: guide\framework\template\computed.html

# # 计算属性

# [#](computed.html#计算属性) 计算属性
 ## [#](computed.html#计算属性的基本使用) 计算属性的基本使用
 我们经常会在模版内使用表达式，合理使用表达式可以提升我们的开发效率。但是在模板中放入太多的逻辑会让模板过重且难以维护。例如：
 ```
 {{ message.split('').reverse().join('') }} 

```
在这个地方，模板不再是简单的声明式逻辑。如果我们在很多地方用上这样的表达式，有可能让模版的可读性降低，并且重复使用同一表达式逻辑会让代码变得冗余，不易维护。对此，我们可以使用计算属性：
 ```

      Original message: "{{ message }}"
      Comtextuted reversed message: "{{ reversedMessage }}"

    export default {
      data: {
        message: 'Hello'
      },
      computed: {
        // 计算属性的 getter
        reversedMessage() {
          // `this` 指向 vm 实例
          return this.message.split('').reverse().join('')
        }
      },
      onReady() {
        console.log(this.reversedMessage) // olleH
    
        this.message = 'Goodbye'
    
        console.log(this.reversedMessage) // eybdooG
      }
    }

```
这里我们声明了一个计算属性 `reversedMessage`。我们提供的函数将用作属性 `vm.reversedMessage` 的 getter 函数，这时 `vm.reversedMessage` 的值始终取决于 `vm.message` 的值。
 你可以像绑定普通属性一样在模板中绑定计算属性。组件实例知道 `vm.reversedMessage` 依赖于 `vm.message`，因此当 `vm.message` 发生改变时，所有依赖 `vm.reversedMessage` 的绑定也会更新。
 ## [#](computed.html#设置计算属性的-setter-函数) 设置计算属性的 `setter` 函数
 计算属性默认只有 getter ，不过在需要时你也可以提供一个 setter ：
 ```

    export default {
      data: {
        firstName: 'Quick',
        lastName: 'App'
      },
      computed: {
        fullName: {
          get() {
            return `${this.firstName} ${this.lastName}`
          },
          set(value) {
            const names = value.split(' ')
            this.firstName = names[0]
            this.lastName = names[names.length - 1]
          }
        }
      },
      onReady() {
        console.log(this.fullName) // Quick App
        this.fullName = 'John Doe'
        console.log(this.firstName) // John
        console.log(this.lastName) // Doe
      }
    }

```


---
## 文件: guide\framework\template\event.html

# # 事件绑定

# [#](event.html#事件绑定) 事件绑定
 ## [#](event.html#格式) 格式
 ```

```
可以简写为：
 ```

```
**fn**：`fn`为事件回调函数名（在``中有对应的函数实现），上例中`press`为事件回调函数。
 ## [#](event.html#传参) 传参
 ### [#](event.html#常量) 常量
 ```

    {{$item}}

  export default {
    private: {
      list:[1,2,3,4,5]
    },
    handle(idx,item,$evt) { // 点击第一个元素
      console.log(idx) // 0
      console.log(item) // 1
      console.log($evt) // { pageX: 4, pageY: 246, clientX: 4, clientY: 246, offsetX: 4, offsetY: 246 }
    }
  }

```
### [#](event.html#变量) 变量
 ``中定义的页面的数据变量（前面不用写`this`）。
 ```

     {{$item}}

  export default {
    private: {
      list:[1,2,3,4,5],
      total:0
    },
    handle(total,num,$evt){
      console.log(total)
      console.log(num)
      console.log($evt)
    }
  }

```
注意
 回调函数被调用时，会在参数列表末尾自动添加一个`evt`参数，通过`evt`参数访问回调事件相关上下文数据。


---
## 文件: guide\framework\template\for.html

# # 循环指令

# [#](for.html#循环指令) 循环指令
 ## [#](for.html#列表渲染) 列表渲染
 如果要实现列表渲染，我们会用到 for 指令。for 指令用于循环输出一个数组类型的数据。
 提示
 `for指令`根据源数据数组渲染列表，支持的写法如下（其中`{{}}`可以省略）：

- `for="{{list}}"` `list`为源数据数组，默认的数组元素名为`$item`；
 - `for="{{value in list}}"` `value`为自定义的数组元素名，默认的数组元素索引名为`$idx`；
 - `for="{{(index, value) in list}}"` `index`为自定义的数组元素索引名，`value`为自定义的数组元素名。

 提示
 `for指令`的`tid属性`用于指定数组元素的唯一 Id，若未指定，默认使用数组索引(`$idx`)作为唯一 Id。`tid属性`的作用在于元素节点重用，优化 for 循环的重绘效率

 **示例如下：**
 ```

      {{$idx}}.{{$item.name}}

      {{$idx}}.{{value.name}}

      {{personIndex}}.{{personItem.name}}

  .page {
    flex-direction: column;
  }

  .row {
    width: 85%;
    margin-top: 10px;
    margin-bottom: 10px;
  }

  export default {
    private: {
      list: [
        { name: 'aa', uniqueId: 1 },
        { name: 'bb', uniqueId: 2 },
        { name: 'cc', uniqueId: 3 }
      ]
    },
    onInit() {
      console.info('指令for')
    }
  }

```
示例代码中，在渲染页面时，`div.row`的结构，会根据 script 中的数据 list 的定义，被循环的生成多个。
 `tid="uniqueId"`，数组元素的某个属性名，不一定叫`uniqueId`。它类似于React的`key={item.uniqueId}`或vue的`:key="item.uniqueId"`，用于优化渲染速度。
 当数据修改时，数据不改变的dom不会被重新渲染，已经改变的数据所在的dom才会被重新渲染，因此我们必须保证`uniqueId`这个属性值在每个数组元素都不一样。
 注意
 
1. for 指令只能循环数组，不能循环对象。

 1. for 指令在 block 标签的行为与它在其他标签的行为不一样，block 标签在渲染时不会额外插入DOM节点，而是会循环其内部的DOM结构，其他标签则会循环生成其自身。

 1. 当 for 指令与 if 指令共存于一个标签时， if 指令的优先级优于 for 指令。为了方便未看文档的新人快速上手项目，不建议这两个指令共存于同一个标签。

 1. 自定义变量表示 for 指令的数组索引和数组元素时，变量名不可以用`$`或`_`开头。

 1. 使用`tid属性`时应注意：
 
- `tid属性`指定的数据属性必须存在，否则可能导致运行异常；
 - `tid属性`指定的数据属性要保证唯一，否则可能导致性能问题；
 - `tid属性`目前不支持表达式。


---
## 文件: guide\framework\template\if.html

# # 条件指令

# [#](if.html#条件指令) 条件指令
 ## [#](if.html#if指令) if指令
 if 条件指令，是指 if/elif/else 这 3 个相关指令，用于控制是否增加或者删除组件；
 **if/elif/else 节点必须是相邻的兄弟节点**。
 ```

    Hello-1
    Hello-2
    Hello-3

  export default {
    // 页面级组件的数据模型，影响传入数据的覆盖机制：private内定义的属性不允许被覆盖
    private: {
      display: false
    }
  }

```
## [#](if.html#show指令) show指令
 show 指令，是指是否显示组件，用于控制组件的显示状态，并不会从 DOM 结构中删除；
 show等同于 visible=none，主要用于在原生组件上声明；
 ```

  Hello

  export default {
    // 页面级组件的数据模型，影响传入数据的覆盖机制：private内定义的属性不允许被覆盖
    private: {
      visible: false
    }
  }

```
## [#](if.html#if与show区别) if与show区别
 
- 当 if/elif 指令的值为 false 时，节点会从页面中移除，当 if/elif 指令值为 true，组件动态插入 DOM 结构中；

 - 当 show 指令的值为 true 时，节点可见，当其值为 false 时，组件不可见，但节点仍会保留在页面 DOM 结构中。


---
## 文件: guide\framework\template\index.html

# # template 模板

# [#](index.html#template-模板) template 模板
 类似`HTML`的标签语言，结合基础组件、事件，构建出页面的结构。
 注意
 模板中只能有 1 个根节点，如：div；请不要在``下存在多个根节点，也不要使用 block 作为根节点。

 ## [#](index.html#数据绑定) 数据绑定
 ```

  {{message}}

  export default {
    // 页面级组件的数据模型，影响传入数据的覆盖机制：private内定义的属性不允许被覆盖
    private: {
      message: 'Hello'
    }
  }

```
## [#](index.html#事件绑定) 事件绑定
 ```

  export default {
    press(e) {
      this.title = 'Hello'
    }
  }

```
事件回调支持的写法（其中`{{}}`可以省略）：
 **fn**：`fn`为事件回调函数名（在``中有对应的函数实现）；
 **fn(a,b)**：函数参数例如`a`，`b`可以是常量，或者是在``中定义的页面的数据变量（前面不用写`this`）；
 回调函数被调用时，会在参数列表末尾自动添加一个`evt`参数，通过`evt`参数访问回调事件相关上下文数据（数据内容具体参看组件回调事件说明），例如点击事件的点击位置`x`，`y`。
 ## [#](index.html#列表渲染) 列表渲染
 ```

      {{$idx}}
      {{$item.uniqueId}}

  export default {
    // 页面级组件的数据模型，影响传入数据的覆盖机制：private内定义的属性不允许被覆盖
    private: {
      list: [
        { uniqueId: 1 },
        { uniqueId: 2 }
      ]
    }
  }

```
`for指令`根据源数据数组渲染列表，支持的写法如下（其中`{{}}`可以省略）：
 
- `for="{{list}}"` `list`为源数据数组，默认的数组元素名为`$item`；
 - `for="{{value in list}}"` `value`为自定义的数组元素名，默认的数组元素索引名为`$idx`；
 - `for="{{(index, value) in list}}"` `index`为自定义的数组元素索引名，`value`为自定义的数组元素名。

 `for指令`的`tid属性`用于指定数组元素的唯一 Id，若未指定，默认使用数组索引(`$idx`)作为唯一 Id。`tid属性`的作用在于元素节点重用，优化 for 循环的重绘效率。
 示例代码中，`tid="uniqueId"`表示使用数组`list`的数组元素`$item.uniqueId`作为数组元素的唯一 Id，且必须保证 uniqueId 这个属性值在每个数组元素都不一样。
 使用`tid属性`时应注意：
 
- `tid属性`指定的数据属性必须存在，否则可能导致运行异常；
 - `tid属性`指定的数据属性要保证唯一，否则可能导致性能问题；
 - `tid属性`目前不支持表达式。

 ## [#](index.html#条件渲染) 条件渲染
 分为2种：`if/elif/else` 和`show`。它们的区别在于：`if`为`false`时，组件会从VDOM中移除，而`show`仅仅是渲染时不可见，组件依然存在于 VDOM 中；
 `if/elif/else`节点必须是相邻的兄弟节点，否则无法通过编译。
 ```

    Hello-1
    Hello-2
    Hello-3

  export default {
    // 页面级组件的数据模型，影响传入数据的覆盖机制：private内定义的属性不允许被覆盖
    private: {
      display: false
    }
  }

```
`show`等同于`visible=none`，主要用于在原生组件上声明；
 ```

  Hello

  export default {
    // 页面级组件的数据模型，影响传入数据的覆盖机制：private内定义的属性不允许被覆盖
    private: {
      visible: false
    }
  }

```
## [#](index.html#逻辑控制块) 逻辑控制块
 可以使用``实现更为灵活的循环/条件渲染；注意``目前只支持`for`和`if/elif/else`属性，如果没有指定任何属性，``则在构建时被当作`透明`节点对待，其子节点被添加到``的父节点上。
 ```

        {{$item.name}}

        {{$item.address}}

  export default {
    // 页面级组件的数据模型，影响传入数据的覆盖机制：private内定义的属性不允许被覆盖
    private: {
      cities: [
        {
          name: 'beijing',
          spots: [
            { address: 'XXX' }
          ]
        },
        {
          name: 'shanghai',
          spots: [
            { address: 'XXX' },
            { address: 'XXX' }
          ]
        }
      ]
    }
  }

```


---
## 文件: guide\framework\template\Props.html

# # Props

# [#](Props.html#props) Props
 ## [#](Props.html#prop-写法) Prop 写法
 Prop 属性名称使用 camelCase(驼峰命名法)命名，在外部传递数据时请转化为以 kebab-case (短横线分隔命名) propObject 转换为 prop-object。
 ```

    {{ propObject.name }}

  export default {
    props: ['propObject']
  }

```
```

  export default {
    private: {
      obj: {
        name: 'child-demo'
      }
    }
  }

```
## [#](Props.html#属性默认值) 属性默认值
 子组件声明属性时，可以设置默认值。当调用子组件没有传入该数据时，将会自动设为默认值。
 如果需要设置默认值，`props` 属性的写法必须要要写成 Object 形式，不能写成 Array 形式。
 **示例如下：**
 ```

  // 子组件
  export default {
    props: {
      prop1: {
        default: 'Hello' //默认值
      },
      prop2Object: {} //不设置默认值
    },
    onInit() {
      console.info(`外部传递的数据：`, this.prop1, this.prop2Object)
    }
  }

```
## [#](Props.html#数据单向性) 数据单向性
 父子间的数据传输是单向性的，父组件 prop 数据更新，子组件的数据会刷新为最新值；子组件的 prop 值发生改变，并不会改变父组件中值。
 但是**prop 类型事数组或者对象，自组件变化会影响到父组件的值，**这意味着你不应该在一个子组件内部改变 prop 的值，这是危险性操作。
 ## [#](Props.html#常见的三种操作-prop-值的方法) 常见的三种操作 prop 值的方法：
 ### [#](Props.html#_1-prop-传入的值作为初始值-用-data-接收) 1. prop 传入的值作为初始值，用 data 接收
 ```

  export default {
    props: ['say', 'propObject'],
    data{
      count: null,
      obj: null
    },
    onInit() {
      console.info(`外部传递的数据：`, this.say, this.propObject)

      this.count = this.propObject.count // 将prop中一个简单类型赋值给data
      this.obj = JSON.parse(JSON.stringify(this.propObject)) // 将 prop 深度克隆
    }
  }

```
### [#](Props.html#_2-watch-监控数据改变) 2. $watch 监控数据改变
 如果是监听对象中的属性，参数请使用.分割，如：$watch(xxx.xxx.xxx, methodName)，详见[$watch](../script/global-data-method.html#this.$watch)。
 ```

  export default {
    props: ['propObject'],
    data {
      propSay: ''
    },
    onInit() {
      // 监听数据变化
      this.$watch('propObject.name', 'watchPropsChange')
    },
    /**
     * 监听数据变化，你可以对数据处理后，设置值到data上
     * @param newV
     * @param oldV
     */
    watchPropsChange(newV, oldV) {
      console.info(`监听数据变化：`, newV, oldV)
      this.propSay = newV && newV.toUpperCase()
    }
  }

```
### [#](Props.html#_3-computed-属性) 3. computed 属性
 详见[计算属性](computed.html)。
 ```

  export default {
    props: [
      'say'
    ],
    computed:{
      sayText() {
        return this.say.toUpperCase()
      }
    }
  }

```
## [#](Props.html#属性校验) 属性校验
 子组件声明属性时，可以指定数据类型校验。
 类型检查支持的类型包括 [`String`、`Number`、`Boolean`、`Function`、`Object`、`Array`]
 如果需要校验类型，`props` 属性的写法必须要要写成 Object 形式，不能写成 Array 形式。
 **示例如下：**
 ```

  // 子组件
  export default {
    props: {
      prop1: {
        default: 'Hello' //默认值
        type: String // 校验类型
      },
      prop2Object: {} //不设置默认值
    },
    onInit() {
      console.info(`外部传递的数据：`, this.prop1, this.prop2Object)
    }
  }

```


---
## 文件: guide\multi-screens\conditional-compilation.html

# # 条件编译

# [#](conditional-compilation.html#条件编译) 条件编译
 基于设备特性的条件编译是一种在编译过程中根据设备类型和屏幕形状等特征来决定哪些代码应该被包含在应用程序中的技术。这项功能适用于跨设备应用开发，其中不同的设备可能需要不同的代码逻辑和界面布局。目前支持条件编译的文件类型有：ux、js。
 ## [#](conditional-compilation.html#流程) 流程
 
1. 安装 conditional-compilation-webpack-plugin

 ```
npm install conditional-compilation-webpack-plugin -D

```

1. 安装 cross-env

 ```
npm install cross-env -D

```

1. 在项目根目录中增加 quickapp.config.js 文件，并参考如下配置

 ```
const ConditionalCompilationWebpackPlugin = require("conditional-compilation-webpack-plugin")

module.exports = {
  plugins: [new ConditionalCompilationWebpackPlugin()]
}

```

1. 根据设备特征，在 package.json 中添加脚本

 ```
"scripts": {
  "build:circle": "cross-env DEVICE_TYPE=WATCH SHAPE=CIRCLE aiot build --enable-custom-component",
  "build:rect": "cross-env DEVICE_TYPE=WATCH SHAPE=RECT aiot build --enable-custom-component"
}

```

1. 在代码中使用条件编译，参考如下代码

 ```

  export default {
    handleSet() {
      // if true: process.env.DEVICE_TYPE === 'WATCH' && process.env.SHAPE === 'CIRCLE'
      console.log('handleSet shape = circle')
      // endif
      // if true: process.env.DEVICE_TYPE === 'WATCH' && process.env.SHAPE === 'RECT'
      console.log('handleSet shape = rect')
      // endif
    }
  };

  .home-page {
    align-items: flex-start;
    justify-content: flex-start;
    padding: 60px;
    position: relative;
  }

  .btn {
    height: 60px;
    width: 360px;
    text-align: center;
    border-radius: 5px;
    margin-bottom: 30px;
    color: #ffffff;
    font-size: 30px;
    background-color: #0faeff;
  }

/* if true: process.env.DEVICE_TYPE === 'WATCH' && process.env.SHAPE === 'CIRCLE' */
  .bg-red {
    background-color: red;
  }
/* endif */
/* if true: process.env.DEVICE_TYPE === 'WATCH' && process.env.SHAPE === 'RECT' */
  .bg-blue {
    background-color: blue;
  }
/* endif */

```

1. 执行配置好的编译命令

 ```
npm run build:circle
或
npm run build:rect

```


---
## 文件: guide\multi-screens\index.html

# # 多屏适配

# [#](index.html#多屏适配) 多屏适配
 ## [#](index.html#为什么要做多屏适配) 为什么要做多屏适配？
 小米可穿戴设备（手环、手表）众多，设备间屏幕形状、尺寸和分辨率存在差异，为了保证应用在不同屏幕上都能正常显示，需要进行多屏适配。
建议开发者在开发应用时尽量考虑多屏适配，提高应用的通用性，以保证应用能分发到尽量多的可穿戴设备上，帮助开发者快速拓展用户。
以下表格是搭载 Vela OS 的设备屏幕详细信息，请参考。
 | 设备类型  | 设备型号  | 屏幕形状  | 屏幕尺寸  | 分辨率  | PPI  | 屏幕宽度DP  | 长宽比  | 手表  | Xiaomi Watch S1 Pro  | 圆形  | 1.47英寸  | 480x480  | 326  | 240  | 1  | 手表  | Xiaomi Watch H1  | 圆形  | 1.43英寸  | 466x466  | 326  | 233  | 1  | 手表  | Xiaomi Watch S3  | 圆形  | 1.43英寸  | 466x466  | 326  | 233  | 1  | 手表  | Xiaomi Watch S4 sport  | 圆形  | 1.43英寸  | 466x466  | 326  | 233  | 1  | 手表  | Xiaomi Watch S4  | 圆形  | 1.43英寸  | 466x466  | 326  | 233  | 1  | 手表  | REDMI Watch 5  | 矩形  | 2.07英寸  | 432x514  | 324  | 216  | 0.8  | 手环  | 小米手环8 Pro  | 矩形  | 1.74英寸  | 336x480  | 336  | 168  | 0.7  | 手环  | 小米手环9  | 胶囊形  | 1.62英寸  | 192x490  | 325  | 96  | 0.4  | 手环  | 小米手环9 Pro  | 矩形  | 1.74英寸  | 336x480  | 336  | 168  | 0.7  | 手环  | 小米手环10  | 胶囊形  | 1.725英寸  | 212x520  | 326  | 106  | 0.4  ## [#](index.html#如何做多屏适配) 如何做多屏适配？
 多屏适配的前提是应用需要进行多屏设计，设计稿需要包含不同屏幕的适配方案，具体设计方法请参考 [多屏设计](../design/multi-screens.html)。
 框架提供了一系列技术规范，帮助开发者按照设计稿完成应用的多屏适配，请参考 [多屏规范](specs.html)。
 我们的 IDE 也提供了多屏 UI 模拟器，帮助开发者快速预览应用在不同屏幕上的效果，以便进行适当调整，请参考 [多屏 UI 模拟器](../../tools/debug/multi-screens.html)。
 我们提供了一些对常见页面元素进行多屏适配的代码示例以及整站案例供参考，[代码示例](samples.html)。


---
## 文件: guide\multi-screens\samples.html

# # 代码示例

# [#](samples.html#代码示例) 代码示例
 ## [#](samples.html#页面布局及元素适配) 页面布局及元素适配
 一些开发中常见的跨屏适配示例。
 ### [#](samples.html#自适应容器大小) 自适应容器大小
 使用百分比或flex样式替代px写固定容器大小的布局方式可以在多屏适时有更好的兼容性。比如长列表滚动的场景，示例如下：
 ```

     长列表 

        {{$item.name}}

  export default {
    private: {
      listData: [
        {
          name: 'TEST1 TEST1 TEST1'
        }, {
          name: 'TEST2 TEST2 TEST2'
        }, {
          name: 'TEST3 TEST3 TEST3'
        }, {
          name: 'TEST4 TEST4 TEST4'
        }, {
          name: 'TEST5 TEST5 TEST5'
        }, {
          name: 'TEST6 TEST6 TEST6'
        }, {
          name: 'TEST7 TEST7 TEST7'
        }, {
          name: 'TEST8 TEST8 TEST8'
        }, {
          name: 'TEST9 TEST9 TEST9'
        }, {
          name: 'TEST10 TEST10 TEST10'
        }
      ]
    }
  }

.demo-page {
  flex-direction: column;
  align-items: center;
  background-color: #fff;
}

.title {
  margin-top: 50px;
  padding: 20px 0;
  font-size: 32px;
}
.list {
  flex: 1;
  width: 340px;
  margin-bottom: 5px;
  align-items: center;
}
.item {
  width: 100%;
  height: 100px;
  margin-bottom: 20px;
  border-radius: 20px;
  background-color: #ccc;
  text-align: center;
}

text {
  width: 100%;
  font-size: 30px;
  text-align: center;
  color: #000;
}

```
效果展示：
 圆形屏幕 / 矩形屏 / 胶囊屏
 ![](../../../images/multi-screens/multi-samples-list-1.png) ![](../../../images/multi-screens/multi-samples-list-2.png) ![](../../../images/multi-screens/multi-samples-list-3.png)
 ### [#](samples.html#单页三行布局) 单页三行布局
 手表、手环场景下单页面三行布局是比较常用的一种设计方式，页面结构大致分为顶部标题栏，底部按钮交互区域以及中部主体内容区。建议采用顶部底部高度固定，主体部分高度自适应的方式来做整体布局。
 代码示例：
 ```

      header

      content

      footer

  export default {}

.demo-page {
  width: 466px;
  height: 466px;
  flex-direction: column;
}

.header {
  width: 100%;
  height: 100px;
  background-color: red;
}

.content {
  flex: 1;
  background-color: yellow;
}

.footer {
  width: 100%;
  height: 100px;
  background-color: blue;
}

text {
  width: 100%;
  font-size: 30px;
  color: black;
  text-align: center;
}

```
效果展示：
 圆形屏幕 / 矩形屏 / 胶囊屏
 ![](../../../images/multi-screens/multi-samples-layout-c.png) ![](../../../images/multi-screens/multi-samples-layout-r.png) ![](../../../images/multi-screens/multi-samples-layout-s.png)
 ### [#](samples.html#px自动缩放计算) px自动缩放计算
 px长度单位会根据配置的项目配置基准宽度进行换算，过程中产生的小数位会做四舍五入处理。因此，在一些需要精准计算的场景中需要考虑到换算带来的误差值（通常为+-1px）。
 比如下面这个示例， 在计算行宽的时候没考虑误差，导致某些设备上产生渲染错行的问题：
 ![](../../../images/multi-screens/multi-samples-px-1.png) ![](../../../images/multi-screens/multi-samples-px-2.png) ![](../../../images/multi-screens/multi-samples-px-3.png)
 代码示例：
 ```

      {{$item}}

  export default {
    private: {
      nums: [1, 2, 3, 4]
    }
  }

.demo-page {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  align-items: center;
}

.item {
  width: 110px;
  height: 110px;
  margin: 2px;
  background-color: #ccc;
}

text {
  color: #000;
  font-size: 30px;
}

```
效果展示：
 圆形屏幕 / 矩形屏 / 胶囊屏
 ![](../../../images/multi-screens/multi-samples-px-4.png) ![](../../../images/multi-screens/multi-samples-px-5.png) ![](../../../images/multi-screens/multi-samples-px-6.png)
 ### [#](samples.html#全屏背景图) 全屏背景图
 使用全屏背景图需要考虑到图片在不同尺寸的屏幕下是否都能有比较好的展示效果。
如果背景图片中有一些交互性或严格要求位置的部分，建议作为单独的元素与背景图拆分处理。
 效果展示：
 圆形屏幕 / 矩形屏 / 胶囊屏
 ![](../../../images/multi-screens/background-image1.png.html) ![](../../../images/multi-screens/background-image2.png.html) ![](../../../images/multi-screens/background-image3.png.html)
 ### [#](samples.html#页面标题栏) 页面标题栏
 页面标题通常位于页面顶部，在不同屏幕形状的设备上，需要考虑内容显示的美观性与完整性。通常在圆屏、胶囊屏等存在边缘剪切的设备上，标题栏会使用多行设计，保证顶部的展示内容长度不会超出屏幕；而在矩形屏幕上则做单行左右布局让整体设计更为舒展。
 代码示例：
 ```

      {{text1}}
      {{text2}}

  export default {
    private: {
      text1: '09:28',
      text2: '文案'
    }
  }

.demo-page {
  justify-content: center;
  background-color: #5c5c5c;
}

.title {
  width: 90%;
}

.title-text {
  font-size: 36px;
  color: #fff;
}

@media (shape: circle){
  .title {
    flex-direction: column;
    align-items: center;
  }
}

@media (shape: rect) {
  .title {
    margin-top: 10px;
    justify-content: space-between;
    align-items: flex-start;
    flex-direction: row-reverse;
  }
  .title-text {
    font-size: 46px;
  }
}

@media (shape: pill-shaped) {
  .title {
    flex-direction: column;
    align-items: center;
  }
  .title-text {
    font-size: 72px;
  }
}

```
效果展示：
 圆形屏幕 / 矩形屏 / 胶囊屏
 ![](../../../images/multi-screens/multi-samples-title-c.png) ![](../../../images/multi-screens/multi-samples-title-r.png) ![](../../../images/multi-screens/multi-samples-title-s.png)
 ## [#](samples.html#跨屏应用项目示例) 跨屏应用项目示例
 ### [#](samples.html#清单应用) 清单应用
 圆形屏幕 / 矩形屏 / 胶囊屏
 ![](../../../images/multi-screens/demo-todoList3.png) ![](../../../images/multi-screens/demo-todoList1.png) ![](../../../images/multi-screens/demo-todoList2.png)
 项目地址：[点击下载  (opens new window)](https://quickapp-vela.cnbj3-fusion.mi-fds.com/quickapp-vela/multi-screen-todoList.zip)
 ### [#](samples.html#计算器) 计算器
 圆形屏幕 / 矩形屏
 ![](../../../images/multi-screens/demo-calculator1.png) ![](../../../images/multi-screens/demo-calculator2.png)
 项目地址：[点击下载  (opens new window)](https://quickapp-vela.cnbj3-fusion.mi-fds.com/quickapp-vela/multi-screen-calculator.zip)


---
## 文件: guide\multi-screens\simulator.html

# # 概述

# [#](../version/APILevel4.html#概述) 概述
 Xiaomi Vela JS 应用是小米公司开发的一种应用类型，它是基于小米的物联网嵌入式软件平台 Xiaomi Vela OS 开发的。本文将介绍 Xiaomi Vela JS 应用的特点、应用场景以及开发支持，帮助开发者更好地了解和使用这一应用类型。
 如果您想快速了解如何开发 Xiaomi Vela JS 应用，并且希望快速上手，请直接访问 [快速入门](../start.html) 章节。
 ## [#](../version/APILevel4.html#应用特点) 应用特点
 Xiaomi Vela JS 应用是一种基于 Xiaomi Vela OS 操作系统的轻量级应用模式，旨在为智能穿戴设备提供更加流畅和便捷的用户体验。它具备以下显著特点：
 
- **轻量化：**
Xiaomi Vela JS 应用采用了轻量级的架构设计，与传统的应用程序相比，Xiaomi Vela JS 应用具有更小的体积，这使得它们能够快速加载和运行，尤其适合内存和处理能力有限的穿戴设备。
 - **跨平台兼容性：**
Xiaomi Vela JS 应用支持跨端运行，开发者可以一次开发，实现在多种设备上的运行，这大大提高了开发效率和应用的普及率。
 - **高性能渲染：**
系统优化了渲染能力，使得应用的动画和交互更为流畅，提升了用户的使用体验。
 - **安全性能：**
Xiaomi Vela OS 通过三重隔离机制确保了应用的安全性，保护了用户数据和设备的安全。
 - **开发支持：**
小米提供了全面的开发支持 Xiaomi Vela JS 应用的开发工具和文档齐全，开发者可以轻松上手，快速构建高质量的应用。小米提供了包括AIoT-IDE在内的一系列开发工具，支持开发者在Ubuntu、Windows、MacOS等操作系统上进行Xiaomi Vela JS 应用的开发和调试。

 ## [#](../version/APILevel4.html#应用场景) 应用场景
 Xiaomi Vela JS 应用的应用场景广泛，已落地多款产品，覆盖了智能穿戴设备上的多种使用情形：
 
- **健康监测：**  应用可以实时监测用户的心率、睡眠质量等健康数据，为用户提供健康建议和预警。
 - **运动辅助：**  在用户进行运动时，应用能够记录运动数据，提供运动指导和健康管理。
 - **消息提醒：**  应用能够显示手机等设备的消息提醒，方便用户在不拿出手机的情况下查看重要信息。
 - **移动支付：**  应用可以集成支付功能，用户可以直接在穿戴设备上完成支付操作，提高支付的便捷性。
 - **智能控制：**  作为智能家居的控制中心，应用可以远程操控家中的智能设备，如灯光、空调等。
 - **日常工具：**  提供天气预报、闹钟、计时器等日常工具功能，满足用户的多样化需求。

 ## [#](../version/APILevel4.html#技术优势) 技术优势
 相较于传统的应用框架，Xiaomi Vela JS 应用具有以下技术优势：
 ### [#](../version/APILevel4.html#前端开发范式) 前端开发范式
 Xiaomi Vela JS 应用采用JavaScript语言开发，并且支持前端MVVM高效的开发范式，响应式UI框架，易学易用。使得开发者可以降低上手难度，缩短开发周期。这种模式贴合主流前端开发者的思维习惯，使得开发者能够快速构建出功能丰富、交互友好的应用，同时降低了学习成本。参考[开发语法](../framework/index.html)。
 ### [#](../version/APILevel4.html#统一的api和组件) 统一的API和组件
 Xiaomi Vela JS 应用提供了统一的[JS接口](../../features/index.html)和[UI组件](../../components/index.html)，使得开发者无需关心底层硬件和操作系统的差异，简化了开发流程，同时保证了应用的质量和用户体验。
 ### [#](../version/APILevel4.html#高性能渲染) 高性能渲染
 
- 通过架构优化，让复杂计算下沉到原生层，解决 JS 语言的性能瓶颈，从而拥有媲美原生的运行效率和流畅体验。
 - 提供丰富的动效能力，包括30+插值和物理动效，可用于过渡、转场等动画效果，使用户界面更加生动、有趣和富有表现力。
 - 充分挖掘硬件性能，最大限度利用 GPU 和 CPU 的硬件加速能力，让复杂UI 界面和动画更加流畅，达到 60 fps 满帧效果。

 ### [#](../version/APILevel4.html#多屏适配) 多屏适配
 [多屏适配](index.html)是Xiaomi Vela JS 应用框架的另一大特色，具体表现在：
 
- **[适配规范](specs.html)：**  框架支持不同形状、尺寸和分辨率的屏幕自适应，确保应用在各种设备上都能提供良好的视觉体验。
 - **[设计规范](../design/multi-screens.html)：**  Vela提供了一套多屏设计的技术规范，帮助开发者按照设计稿完成应用的多屏适配。
 - **[多屏UI模拟器](simulator.html)：**  AIoT-IDE提供了多屏UI模拟器，使开发者能够快速预览应用在不同屏幕上的效果，进行必要的调整。
 - **[适配案例](samples.html)：**  提供了对常见页面元素进行多屏适配的代码示例以及整站案例，供开发者参考和学习。

 ## [#](../version/APILevel4.html#应用开发流程) 应用开发流程
 ### [#](../version/APILevel4.html#环境搭建) 环境搭建
 小米提供了包括AIoT-IDE在内的一系列开发工具，支持开发者在Ubuntu、Windows、MacOS等操作系统上进行Xiaomi Vela JS 应用的开发和调试。
AIoT-IDE是Xiaomi Vela JS 应用的集成开发环境，提供了项目初始化、编码、调试等一系列功能。请参考[安装环境](../start/use-ide.html)初始化项目。
 ### [#](../version/APILevel4.html#应用开发) 应用开发
 初始化项目后请参考[项目结构](../start/project-overview.html)了解项目中各文件和目录的作用。项目由配置文件（manifest.json）、模板代码（ux文件）、 样式代码（css文件）、逻辑代码（js文件）以及资源文件（图片、音频等）组成。请参考[项目配置](../framework/manifest.html)对项目相关信息进行配置。
 应用开发范式遵循声明式UI，和传统Web开发范式类似。项目中的页面以及组件均由`ux`后缀文件编写，文件由[template 模板](../framework/template/index.html)、[style 样式](../framework/style/index.html)和[script 脚本](../framework/script/index.html)3 个部分组成。开发者可以通过编写`ux`文件使用[UI组件](../../components/index.html)、[自定义组件](../framework/template/component.html)和样式描述页面结构和呈现效果，使用js脚本定义页面数据、实现生命周期接口、通用方法、事件处理等。
请参考[编写页面UI](../start/user-interface.html)进一步了解页面开发。
 ### [#](../version/APILevel4.html#运行调试) 运行调试
 AIoT-IDE提供内置的模拟器，支持开发者启动模拟器在IDE中直接运行和调试应用，实时查看运行效果。请参考[运行调试](../start/use-ide.html#_5-运行项目.html)了解如何运行和调试应用。
 ### [#](../version/APILevel4.html#打包发布) 打包发布
 应用开发完成后，开发者可以使用AIoT-IDE提供的打包功能将应用打包成安装包，请参考[打包项目](../start/use-ide.html#_7-打包项目.html)了解如何打包应用。项目打包成功后请参考[发布](../publish/index.html)进行应用发布。


---
## 文件: guide\multi-screens\specs.html

# # 适配规范

# [#](specs.html#适配规范) 适配规范
 Vela OS 支持一系列适配多种屏幕的技术能力。
 ## [#](specs.html#自适应布局) 自适应布局
 系统提供的容器组件均默认遵循 Flex 弹性布局规则，使用弹性布局可以实现屏幕自适应布局。
 比如以下代码可以实现行内多个 item 平均分布。
 ```

  1
  2
  3

```
![](../../../images/multi-screens/flex-items.png)
 更多说明请参考[Flex 布局示例](../framework/style/page-style-and-layout.html#flex-布局示例)
 ## [#](specs.html#自适应单位) 自适应单位
 在编写 UI 样式时，可以采用系统提供的自适应长度单位，包括：
 
- px
 - %

 ### [#](specs.html#px) px
 px 在 Vela 应用中不表示屏幕的物理像素，而是相对于项目配置基准宽度的单位，其原理类似于rem。
 开发者在 manifest 文件中将 designWidth 字段配置为设计基准宽度（设计稿宽度），然后在样式描述中使用该长度单位，数值直接使用设计稿中的像素值，系统将自动计算使 Vela 应用 UI 在不同屏幕上进行等比缩放。
 ```
{
  "config": {
    "designWidth": 336
  }
}

```
```

.demo-page {
  justify-content: center;
  align-items: center;
}
.container {
  width: 168px;
  height: 168px;
  background-color: aquamarine;
}

```
如上示例中将 designWidth 配置为 336px，那么所有的 px 值使用都会按照 336px 的基准宽度换算。
假设设备屏幕实际宽度为 336 像素，则 container 元素的实际宽度也为 168 像素；如果设备屏幕实际宽度为 192 像素，则 container 元素的实际宽度为 96 像素。
 336*480 屏幕 / 192*490 屏幕
 ![](../../../images/multi-screens/px-demo2.png) ![](../../../images/multi-screens/px-demo1.png)
 更多说明请参考[长度单位](../framework/style/page-style-and-layout.html#长度单位)
 ### [#](specs.html#百分比) 百分比%
 % 表示百分比，许多样式属性可以取百分比值，经常用以根据父对象来确定大小。
 比如以下代码可以实现行内多个 item 按百分比占据父容器宽度，
 ```

  1
  2
  3

```
![](../../../images/multi-screens/percent.png)
 更多说明请参考[CSS 百分比单位  (opens new window)](https://developer.mozilla.org/zh-CN/docs/Web/CSS/percentage)
 ## [#](specs.html#固定长度单位) 固定长度单位[3+](../version/APILevel3.html)
 在有的布局场景下，需要使用固定长度单位，系统支持的 dp 长度单位可满足这个需求。
 DP 长度单位表示设备独立像素（device-independent pixel），也叫密度无关像素，可以认为是计算机坐标系统中的一个点，这个点代表一个可以由程序使用的逻辑像素，是一个近似物理尺寸的单位，其计算公式为：
 ```
屏幕宽度的 DP 值 = 屏幕分辨率的宽度 / DPR
元素宽/高度的 DP 值 = 元素宽/高度的物理像素数 / DPR

```
DPR 表示设备像素比（device pixel ratio），是设备物理像素和逻辑像素（DP）的比值，其计算公式为：
 ```
DPR = 设备 PPI / 160

```
PPI（pixels per inch）表示每英寸的像素数，表征屏幕的物理密度，因此 DPR 又被称为逻辑密度。
设备屏幕的逻辑密度值（DPR）可以通过 device 接口获取。
 比如以下代码可以实现元素在不同尺寸屏幕上保持近似的物理尺寸，从而在大屏幕上呈现更多的元素。
 ```

        A

        B

        C

        D

  .container {
    justify-content: center;
    align-items: center;
  }
  .text-box {
    justify-content: center;
    flex-wrap: wrap;
  }
  text {
    width:116dp;
    height: 30dp;
    font-size: 15dp;
    text-align: center;
  }

```
466*466 屏幕 / 192*490 屏幕
 ![](../../../images/multi-screens/dp-demo1.png) ![](../../../images/multi-screens/dp-demo2.png)
 ## [#](specs.html#媒体查询) 媒体查询
 媒体查询是 CSS3 引入的一个功能，用于根据不同的屏幕尺寸和设备类型，为网页应用不同的样式。
 在 Vela JS 应用中，也可以使用类似的媒体查询规范来针对不同屏幕和设备编写样式，详细介绍请参考[媒体查询](../framework/style/media-query.html)。
 比如以下代码可以实现根据屏幕形状来应用不同的样式。
 注意: 以下示例代码的`designWidth`为`466`
 ```

    10:30
    我的待办

  /* 当屏幕为圆形屏幕时 */
  @media screen and (shape: circle) {
    .container {
      padding-left: 80px;
      padding-right: 80px;
      padding-top: 40px;
      flex-direction: row;
      align-items: flex-start;
      justify-content: space-between;
    }
    text {
      font-size: 40px;
    }
  }
  /* 当屏幕为胶囊形屏幕时 */
  @media screen and (shape: pill-shaped) {
    .container {
      padding-top: 50px;
      flex-direction: column;
      align-items: center;
    }
    text {
      margin-top: 10px;
    }
  }

```
466*466 屏幕 / 192*490 屏幕
 ![](../../../images/multi-screens/mediaquery-demo2.png) ![](../../../images/multi-screens/mediaquery-demo1.png)
 ## [#](specs.html#获取屏幕信息) 获取屏幕信息
 在 Vela JS 应用中，可以通过 device feature 接口获取屏幕信息，包括屏幕形状、屏幕分辨率等。根据获取到的结果可以进行相应的样式适配。
 比如以下代码可以实现根据屏幕形状决定 progress 组件的类型（是否是弧形）。
 ```

  import device from '@system.device'

  export default {
    data: {
      progressType: "horizontal"
    },
    onInit() {
      const that = this
      device.getInfo({
        success: function(ret) {
          that.progressType = ret.screenShape === "circle" ? "arc" : "horizontal"
        }
      })
    }
  }

  .container {
    padding: 20px;
  }

```
圆形屏幕 / 矩形屏幕
 ![](../../../images/multi-screens/shape-circle.png) ![](../../../images/multi-screens/shape-rect.png)
 详细介绍请参考 [设备信息 device](../../features/basic/device.html)


---
## 文件: guide\other\faq.html

# # 常见问题

# [#](faq.html#常见问题) 常见问题
 ## [#](faq.html#如何适配不同尺寸的屏幕) 如何适配不同尺寸的屏幕？
 框架默认的屏幕分辨率是480*480，Vela三方应用会自动适配，开发者可以直接按照设计稿的尺寸来开发。
比如，设计稿是466*466，可以在`manifest.json`中配置`designWidth: 466`，然后css中尺寸相关的数值跟设计稿保持一致即可。
更多详细细节信息可以参考：[页面样式和布局](../framework/style/page-style-and-layout.html)。
 ## [#](faq.html#模拟器怎么跟手表通信) 模拟器怎么跟手表通信？
 模拟器跟手机通讯，需要外接蓝牙适配器，并且配置比较复杂，建议使用真机调试。
 ## [#](faq.html#如何解决通信过程中提示签名不正确的问题) 如何解决通信过程中提示签名不正确的问题？
 手表和手机通信前会检查应用的签名，如果签名不正确通信会被拒绝。所以调试通信时需要手机app和手表rpk使用配套的证书打包。

遇到签名不正确的错误时，请检查导出rpk时使用的证书是否和打包手机app时的证书相同。
 ## [#](faq.html#如何排查通信-interconnect-相关的问题) 如何排查通信(interconnect)相关的问题？
 首先检查手表端发送数据的数据结构是否正确（请参考发送数据）、send方法回调行数执行情况。
其次可以排查手机端打印的日志（使用adb logcat工具），看手机端接受的数据情况。
 ## [#](faq.html#如何解决列表数据更新时闪烁的问题) 如何解决列表数据更新时闪烁的问题？
 通过for循环渲染的列表，在数据更新时，如果出现闪烁，可以增加tid来解决。详细文档可以参考：[循环指令](../framework/template/for.html)。
 ## [#](faq.html#构建release版本rpk时打包证书有什么要求) 构建release版本rpk时打包证书有什么要求？
 
1. 如果涉及手表跟手机通信，打包rpk时的证书需要跟打包手机app的证书一致，否则无法通信；
 1. 如果不涉及通信，对证书无特殊要求，按照文档中的步骤生成即可；

 > 注意：请妥善保管证书，并且保证每次使用相同的证书打release版本rpk包。如果证书改变，可能无法上架。
>
 ## [#](faq.html#如何解决手表和手机连接状态获取问题) 如何解决手表和手机连接状态获取问题？
 进入页面直接获取状态往往会拿到`DISCONNECTED`，因此需要轮询获取状态，根据`getApkStatus()`的返回值，判断手表和手机的连接状态。
 ## [#](faq.html#模拟器支持哪些平台) 模拟器支持哪些平台？
 模拟器支持Windows，Mac和Ubuntu三个平台，其中Windows支持Win10+，Mac支持macOS12+
 ## [#](faq.html#windows和mac是否可以打包rpk) Windows和Mac是否可以打包rpk？
 Windows和Mac可以打包rpk。
 ## [#](faq.html#如何将rpk上传到手表真机运行) 如何将rpk上传到手表真机运行?
 
1. 手机安装小米运动健康(目前是通过商务拉群对接的方式提供。开发vela三方应用需求，请邮件联系常健：[changjian@xiaomi.com](mailto:changjian@xiaomi.com))；
 1. 点击【小米运动健康】-->【我的】-->【关于】-->【Debug】；
 1. 点击【第三方应用】；
 1. 点击【Click to input package name】；
 1. 随便输个字符（只有卸载时要详细包名）；
 1. 选择【Install third app】；
 1. 选择本地rpk文件安装；
 1. 安装成功会有Toast提示。

 ## [#](faq.html#如何查看手表真机上的日志) 如何查看手表真机上的日志？
 
1. 手机安装小米运动健康(目前是通过商务拉群对接的方式提供。开发vela三方应用需求，请邮件联系常健：[changjian@xiaomi.com](mailto:changjian@xiaomi.com))；
 1. 小米运动健康与手表进行同步；
 1. 在手表上复现问题；
 1. 点击【小米运动健康】-->【我的】-->【关于】-->【Debug】-->【拉取固件日志】；
 1. 拉取成功后保存在手机，日志文件目录: `/sdcard/Android/data/com.mi.health/files/log`。


---
## 文件: guide\other\tips.html

# # 注意事项

# [#](tips.html#注意事项) 注意事项
 ## [#](tips.html#手表中的异常场景) 手表中的异常场景
 
1. 网络异常，在没有网络的情况下提示
 1. 数据异常（没有获取到数据，或者后端接口返回错误）处理
 1. JS代码错误处理
 1. 按钮防止重复点击（点击后发请求的操作尤其要注意）
 1. 息屏后重新亮屏会重新触发onShow生命周期函数，此生命周期函数中如果有fetch请求，亮屏时会再次发起请求，需谨慎使用

 ## [#](tips.html#代码规范) 代码规范
 
1. app.ux文件中的代码，必须写到``中，否则代码不会执行！
 1. *.ux文件中，`template`节点只能有一个根节点
 1. 角度相关的css属性必须书写单位，比如`total-angle: 360deg`
 1. `list-item`中，谨慎使用`if`/`else`/`show`等条件判断，保证所有的`list-item`结构一致
 1. `image`的`src`属性不要使用变量拼接（比如 `src="/common/{{type}}`），否则编译器打包代码会显示警告，建议直接使用变量`src="{{imgPath}}"`

 ## [#](tips.html#常见优化) 常见优化
 
1. 减少网络请求次数和并发数
 1. 数据实时性要求不高的接口考虑做本地缓存（缓存也要考虑数据大小）
 1. 控制本地文件数量，避免直接遍历文件获取所有文件大小
 1. 尽可能使用低分辨率的网络图片
 1. 列表使用分页，每一页保持在20个item以内比较好
 1. 网络请求的数据，不要直接存储在内存中，只存储需要用到的字段
 1. 谨慎使用三方依赖，使用轻量级的依赖
 1. 公共代码可以考虑放到全局，不要多次引入
 1. 添加loading态，防止按钮频繁点击后发起多次网络请求


---
## 文件: guide\publish\acceptance-criteria.html

# # 验收标准

# [#](acceptance-criteria.html#验收标准) 验收标准
 为持续提升Vela快应用的用户体验，现正式将启动性能纳入质量验收标准：快应用首页渲染完成时间（FMP）≤2000ms。我们同步整理了快应用最佳实践，包含启动加速方案、资源加载策略等。开发者可参考：[最佳实践](../best-practice/index.html)进行针对性优化。


---
## 文件: guide\publish\index.html

# # 发布

# [#](index.html#发布) 发布
 目前需要联系工作人员[zhangyuanpu@xiaomi.com](mailto:zhangyuanpu@xiaomi.com)进行合作发布。


---
## 文件: guide\start\add-interactivity.html

# # 添加交互

# [#](add-interactivity.html#添加交互) 添加交互
 在上一章节中，我们已经编写了两个页面的结构和样式，但是两个页面之间没有任何关联，
在本章中，我们会实现页面间的跳转。
 ## [#](add-interactivity.html#注册事件) 注册事件
 页面跳转由用户触发，需要给页面的特定元素添加对应的事件，比如`click`/`touchstart`。
有关事件更多的细节，请参考[通用事件](../../components/general/events.html)。
 在这个天气预报App中，我们采用的交互是滑动切换页面：
 
1. 在实时天气页面中，向上滑动进入未来3天天气页面；
 1. 在未来3天天气页面，向右滑动返回到实时天气页面。

 我们希望在页面任何地方滑动都执行跳转动作，所以将滑动事件(`swipe`)注册到根节点上。
 模板代码如下：
 ```

```
说明
 `@swipe="toListPage"`也可以写成`onswipe="toListPage"`，详情请参考[事件绑定](../framework/template/event.html)。

 ## [#](add-interactivity.html#页面跳转) 页面跳转
 注册完事件后，需要在JavaScript代码中，定义`toListPage()`回调方法，通过判断滑动方向，决定是否做页面跳转。
页面跳转，需要使用到`@system.router`模块，使用前请先在`manifest.json`中声明：
 ```
{
  // ...
  "features": [
    { "name": "system.router" }
  ]
}

```
说明
 更多router相关细节，请参考[页面切换](../framework/page-switch.html)。

 声明模块后，即可在JavaScript脚本中引入模块，然后使用`router`提供的API在页面间跳转：
 ```

  import router from '@system.router'

  export default {
    // ...
    toListPage(eve) {
      if (eve.direction === 'up') {
        router.push({
          uri: '/pages/list'
        })
      }
    }
  }

```
同样，在未来3天天气页面中，使用相同的方式来实现页面返回逻辑。对应的代码为：
 ```

```
```

  import router from '@system.router'

  export default {
    // ...
    toHomePage(eve) {
      if (eve.direction === 'right') {
        router.back()
      }
    }
  }

```


---
## 文件: guide\start\data-fetch.html

# # 数据获取

# [#](data-fetch.html#数据获取) 数据获取
 在前面几个章节中，已经实现了天气预报App的UI，也添加了基本的交互。但页面中的数据
并非真实数据，接下来我们将从和风天气预报接口中请求真实数据并渲染。
 ## [#](data-fetch.html#请求接口数据) 请求接口数据
 网络请求需要使用到[数据请求fetch](../../features/network/fetch.html)模块，
使用之前请在`manifest.json`中声明。
 `fetch`模块提供了`fetch()`方法，详细使用方法请参考Vela文档。
 每个页面都具有自己的[生命周期](../framework/script/lifecycle.html)，可以在`onReady`中发送请求，
代码如下：
 ```

  import router from '@system.router';
  import fetch from '@system.fetch';

  export default {
    private: {
      // ...
    },

    onReady() {
      let key = '';
      // location，这里使用的是武汉的code
      // 更多的location code可以查看：https://github.com/qwd/LocationList
      fetch.fetch({
        url: `https://devapi.qweather.com/v7/weather/now?location=101010100&key=${key}`
      }).then(res => {
        const result = res.data;
        console.log('返回的数据:', JSON.stringify(result.data, null, 2));
      }).catch(error => {
        console.log(`数据请求失败:`, error);
      })
    },

    // ...
  }

```
> 使用之前，请到和风天气开发平台中申请key，然后替换上面代码中的key。
>
 添加上述代码并替换合法的key后，运行App，可以在控制台中看到类似下面的日志，说明请求成功。
 ```
返回的数据: {
  "code": "200",
  "updateTime": "2022-01-04T10:07+08:00",
  "fxLink": "http://hfx.link/2ax1",
  "now": {
    "obsTime": "2022-01-04T09:54+08:00",
    "temp": "-1",
    "feelsLike": "-4",
    "icon": "100",
    "text": "晴",
    "wind360": "45",
    "windDir": "东北风",
    "windScale": "2",
    "windSpeed": "7",
    "humidity": "55",
    "precip": "0.0",
    "pressure": "1029",
    "vis": "15",
    "cloud": "10",
    "dew": "-17"
  },
  "refer": {
    "sources": [
      "QWeather",
      "NMC",
      "ECMWF"
    ],
    "license": [
      "no commercial use"
    ]
  }
}

```
## [#](data-fetch.html#数据渲染) 数据渲染
 从后端请求到数据后，还需要将请求到的数据在页面上显示出来。要显示数据，只需要更改
[页面数据对象](../framework/script/page-data.html)上的对应数据即可：
 ```
this.weather = result.data.now;

```
最终页面完整的JavaScript代码如下：
 ```

  import router from '@system.router';
  import fetch from '@system.fetch';

  export default {
    private: {
      city: '武汉市',
      province: '湖北省',
      country: '中国',
      weather: {
        obsTime: "12-21 09:05",
        temp: "13",
        feelsLike: "10",
        icon: "101",
        text: "多云",
        humidity: "72",
        vis: "16"
      }
    },

    onReady() {
      let key = '';
      // location，这里使用的是武汉的code
      // 更多的location code可以查看：https://github.com/qwd/LocationList
      fetch.fetch({
        url: `https://devapi.qweather.com/v7/weather/now?location=101200101&key=${key}`
      }).then(res => {
        const result = res.data;
        console.log('返回的数据:', JSON.stringify(result.data, null, 2));
        this.weather = result.data.now;
      }).catch(error => {
        console.log(`数据请求失败:`, error);
      })
    },

    toListPage(eve) {
      console.log(eve);
      if (eve.direction === 'up') {
        router.push({
          uri: '/pages/list'
        })
      }
    }
  }

```
页面运行结果：
 ![页面运行结果](../../../images/guide/api-wuhan-now.png)
 未来3天天气预报界面的数据请求，跟实时天气页面一样，这里不再赘述。
 在真实的项目中，还需要处理更多的页面细节，比如页面请求数据时添加loading状态、对日期做对应的格式化处理等。
 为了给用户提供良好的体验，请严格按照设计稿编写页面结构和样式并对各种异常情况做相应的处理。


---
## 文件: guide\start\project-overview.html

# # 项目结构

# [#](project-overview.html#项目结构) 项目结构
 这个章节将基于[安装环境](use-ide.html)中初始化的项目，
来讲解 Vela JS 应用项目中的各部分的作用。
 ## [#](project-overview.html#目录结构) 目录结构
 Vela JS 应用项目由配置文件（manifest.json）、模板代码（ux文件）、
样式代码（css文件）、逻辑代码（js文件）以及资源文件（图片、音频等）组成。
 典型的项目目录结构如下：
 ```
├── manifest.json
├── app.ux
├── pages
│   ├── index
|   |   └── index.ux
│   └── detail
|       └── detail.ux
├── i18n
|   ├── defaults.json
|   ├── zh-CN.json
|   └── en-US.json
└── common
    ├── style.css
    ├── utils.js
    └── logo.png

```
## [#](project-overview.html#配置文件) 配置文件
 项目根目录中的`manifest.json`文件为项目的配置文件，应用信息、使用到的系统接口以及页面路由等信息需要在这个配置文件中声明。
 详细配置字段说明可以参考[项目配置](../framework/manifest.html)。
 ## [#](project-overview.html#ux模板) ux模板
 一个页面通常都由三部分组成：页面结构、样式和逻辑交互。这三部分，可以放在一个ux文件中，也可以作为独立的文件。
 如果放在一个ux文件中，则ux文件需要包含三标签：`template`、`style`和`script`。
 示例：
 ```

    欢迎打开{{title}}

  .btn {
    width: 400px;
    height: 60px;
    background-color: #09ba07;
    color: #ffffff;
  }

  import router from '@system.router'

  export default {
    // 页面数据对象
    private: {
      title: '示例页面'
    },
    // 按钮点击后的回调
    routeDetail() {
      router.push({
        uri: '/pages/detail'
      })
    }
  }

```
如果将页面结构、样式和逻辑交互分开作为独立的文件，可以使用如下目录结构：
 ```
├── ...
├── pages
│   ├── ...
│   └── detail
|       ├── detail.ux
|       ├── detail.css
|       └── detail.js
├── ...

```
说明
 如果作为独立的文件，将ux/css/js文件分开后，ux文件中不能包含`template`标签。

 ## [#](project-overview.html#app-ux) app.ux
 `app.ux`用于定义App的生命周期、全局数据或者全局方法。
 详细使用方法可以参考[app.ux](../framework/ux.html#appux)。
 ## [#](project-overview.html#common) common
 `common`文件夹主要用来存放公共的资源，比如图片、音频和公共样式等。
 ## [#](project-overview.html#i18n) i18n
 `i18n`文件夹用来存放多语言配置文件。


---
## 文件: guide\start\toolkit-params.html

# # 编译参数

# [#](toolkit-params.html#编译参数) 编译参数
 编译工具提供了多种编译能力，开发者可以根据项目需求进行设置。请**注意**，编译参数仅在 `build`、`server`、`release` 命令中可用
 ## [#](toolkit-params.html#如何设置编译参数) 如何设置编译参数
 通常有两种方式设置编译参数，以抽取单独的 source-map 文件为例：
 
- 在命令行携带编译参数

 ```
aiot build --devtool=source-map

```

- 在项目根目录新建配置文件 quickapp.config.js，并配置 cli 属性；

 ```
module.exports = {
  cli: {
    devtool: "source-map",
  },
};

```
## [#](toolkit-params.html#查看当前工具支持的全部编译参数) 查看当前工具支持的全部编译参数
 ```
 npx aiot build -h

```
## [#](toolkit-params.html#常见编译参数) 常见编译参数
 | 参数名  | 值类型  | 描述  | 默认值  | --devtool  | `string`  | sourcemap 的输出形式， 参数值及含义可以参考 [webpack/devtool  (opens new window)](https://www.webpackjs.com/configuration/devtool/#root) 
 示例：`aiot server --devtool=source-map`  | none  | --enable-jsc  | `boolean`  | 是否将 js 文件将转换为 jsc 文件，以提高运行性能 
 示例：`aiot server --enable-jsc`  | false  | --enable-protobuf  | `boolean`  | 是否启用 protobuf 的二进制打包，以提高运行性能 
 示例：`aiot server --enable-protobuf`  | false  | --enable-custom-component  | `boolean`  | 是否支持自定义组件 
 示例：`aiot server --enable-custom-component`  | false


---
## 文件: guide\start\use-ide.html

# # 使用 AIoT-IDE 来开发 JS 应用

# [#](use-ide.html#使用-aiot-ide-来开发-js-应用) 使用 AIoT-IDE 来开发 JS 应用
 ## [#](use-ide.html#下载-aiot-ide) 下载 AIoT-IDE
 
 本应用支持 **macOS**、**Windows** 及 **Ubuntu** 系统，请确保设备满足以下最低系统版本要求。
 
---
 ## [#](use-ide.html#系统要求) 系统要求
 | 操作系统  | 最低版本要求  | **macOS**  | 14（Sonoma）及以上  | **Windows**  | 10 或更高版本  | **Ubuntu**  | 20.04 LTS 或更高版本  ## [#](use-ide.html#安装-aiot-ide) 安装 AIoT-IDE
 ==mac 版本下可能会遇到如下安装报错的问题:==
 ![alt text](../../../images/ide/ide-download-1.png)
 遇到此类问题，请按下面方法操作：
 1.打开 shell 窗口，输入 ：`sudo xattr -r -d com.apple.quarantine`,如下图示例一。

2.敲入空格 **再将应用程序拖入到窗口中**，会得到下图示例二。

3.点击回车，输入密码，再执行该程序即可。
 ![alt text](../../../images/ide/ide-download-2.png) 示例一

 ![alt text](../../../images/ide/ide-download-3.png) 示例二

 ## [#](use-ide.html#历史版本) 历史版本
 历史版本地址：[点击查看  (opens new window)](https://kpan.mioffice.cn/webfolder/ext/j6SfQsarf8I%40?n=0.18700074913007825)

密码：99E6
 ## [#](use-ide.html#使用-aiot-ide) 使用 AIoT-IDE
 ### [#](use-ide.html#_1-初始化-vela-项目) 1. 初始化 Vela 项目
 
- 通过点击左上角 「文件」 > 「新建项目」 打开项目初始化图形界面
 - 点击卡片左侧边栏的 Vela，点击 「创建」
 - 选择一个项目模版，点击 「下一步」
 - 输入项目名称和项目保存路径后，点击「创建」，等待项目创建完成

 ![](../../../images/tools/ide-create-project.png)
 ![](../../../images/tools/ide-project-template.png)
 ### [#](use-ide.html#_2-项目开发) 2. 项目开发
 支持依赖安装、运行、调试、重启、模拟器管理和打包项目等功能。

打开 Vela 快应用项目后，AIoT-IDE 会弹出顶部的 banner 操作按钮栏以及右侧的开发向导页，可根据开发向导的指引安装相关的依赖。

依赖安装完成后，可点击 banner 栏里的操作按钮实现对应的功能。
 ![](../../../images/tools/ide-warning.png)
 ### [#](use-ide.html#_3-开发向导指引) 3. 开发向导指引
 打开 Vela 快应用项目，AIoT-IDE 右侧会弹出开发向导，用于指导开发。可以根据向导的提示完成对应的操作。
 ![](../../../images/tools/ide-success.png)
 ==按向导提示遇到 npm i 下载 npm 包失败,可按如下方法解决：==
 ![](../../../images/ide/ide-npm-0.png)
 
- 检查当前项目根目录中是否有.npmrc 文件,如果没有则自己创建。
 ![alt text](../../../images/ide/ide-npm-1.png)

 - 打开.npmrc 文件,将下面内容复制到文件中
 `registry="https://registry.npmmirror.com/"`
 ![alt text](../../../images/ide/ide-npm-2.png)

 - 打开终端,在终端重新运行 npm i
 ![alt text](../../../images/ide/ide-npm-3.png)

 ### [#](use-ide.html#_4-模拟环境管理) 4. 模拟环境管理
 AIoT-IDE 支持自动初始化模拟器环境，创建、删除和列表展示模拟器，以便在运行/调试时可以选择不同的模拟器查看效果。
 **模拟器环境说明**
 如果缺少模拟器环境和模拟器实例，开发向导中会给出需相应的提示
 
- 点击下方的「检查模拟器环境，创建模拟器实例」按钮，在弹出的模态窗口中选择 「自动安装」，插件会自动帮助安装模拟器相关的依赖
 - 在模拟器列表页，点击左上角的「创建」按钮去生成一个模拟器实例

 ![](../../../images/tools/ide-warning-1.png)
 **模拟器操作说明**
 
- 查看已创建的模拟器

点击 banner 栏的「模拟器」按钮，进入模拟器列表页，这里可以看到已经创建的模拟器的详细信息

 ![](../../../images/tools/ide-emulator-2.png)
 
- 创建一个新的模拟器

- 点击**设备管理**左上角的**新建**按钮，进入模拟器表单页
 - 在模拟器表单页，填写要创建的模拟器信息，点击「创建」按钮，插件首先会下载 Vela 镜像并完成创建

 ==推荐使用 Vela 正式版（4.0）版本的镜像==
 ![](../../../images/tools/ide-emulator-3.png)
 ### [#](use-ide.html#_5-运行项目) 5. 运行项目
 运行时首先需要选择要在哪个模拟器上预览效果，选择后则会将当前打开的快应用在模拟器中启动并显示项目 UI。
 ![](../../../images/tools/ide-debug-1.png)
 插件会过滤出`$HOME/.android/avd`这个目录下以“Vela”开头的所有模拟器，如果没有找到，AIoT-IDE 右下角会弹出提示，可点击「模拟器」按钮去进行创建。
 ### [#](use-ide.html#_6-调试项目) 6. 调试项目
 跟运行一样，调试时同样需要选择模拟器。

快应用启动成功后，AIoT-IDE 底部会弹出调试面板，点击调试面板的 Tab 栏即可进行对应的操作，比如查看 DOM 树、查看 Console 以及断点调试。
 ![](../../../images/ide/ide-debug-0.png)
 ### [#](use-ide.html#_7-打包项目) 7. 打包项目
 **开发模式打包**

开发完成后，可以点击 banner 栏的「打包」按钮来打包应用，默认会生成两个新的目录：dist、build；

其中 dist 文件夹中会生成一个`.debug.rpk`文件，build 中会生成编译后的 js 文件。
 **生产模式打包**
 
- 打包前需要配置 signature private key ，生成签名文件
 
- 自动生成：点击 banner 栏的「发布」按钮 > 填写相关信息 > 点击「完成」按钮，插件会在项目的 sign 目录下生成`private.pem`和`certificate.pem`两个文件。签名文件生成成功后，点击**顶部操作栏**栏的「发布」按钮，打包生成的产物跟开发模式打包类似，但是 dist 文件夹中的 rpk 文件是 release 后缀。

 ![](../../../images/tools/ide-debug-11.gif)
 
- 自动生成需要系统环境安装了 openssl，windows 系统可能遇到 openssl 缺失的问题，下面是具体解决方法。
 
- 安装 openssl 并配置系统环境变量，并重启电脑。重启成功后，在 AIoT-IDE 中打开终端，输入 openssl，如下图所示，则是安装成功。

 ![](../../../images/ide/ide-openssl.png)

 - 手动生成：前提同样是系统环境安装了 openssl,然后打开终端运行以下命令生成签名文件，项目下新建 sign 目录，将生成的文件 private.pem 和 certificate.pem 放至该目录
 `openssl req -newkey rsa:2048 -nodes -keyout private.pem -x509 -days 3650 -out certificate.pem`


---
## 文件: guide\start\user-interface.html

# # 编写页面UI

# [#](user-interface.html#编写页面ui) 编写页面UI
 在上一节[项目结构](project-overview.html)中，介绍了 Vela JS 应用项目中各文件、目录的作用，对项目结构有了一定的了解之后，接下来我们会实现一个简单的天气预报App。
 这个章节将实现这个App的页面UI，主要内容为：页面的基本结构、样式、数据结构定义以及数据渲染。
 根据UI设计稿还原样式是一个比较细致并且耗时的工作，为了提高工作效率，一般情况下，我们推荐先整体构思页面的实现方式（页面结构、样式和交互等），然后编写页面结构、然后统一添加样式，最后添加数据渲染和交互。
 这个使用指南中，为了方便展示 Vela JS 应用中页面结构，样式和交互的开发，采用了上述的开发流程。开发者在实际开发中，也可以根据自己的开发习惯，选择适合自己的工作流。
 提示
 这部分有比较多的代码片段，如果您已经对HTML/CSS/JavaScript很熟悉，您可以选择跳过此部分内容。
但我们还是建议您阅读本章的内容，以了解 Vela JS 应用开发和传统前端开发的一些区别。

 ## [#](user-interface.html#功能需求) 功能需求
 我们将要实现的天气预报App由两个页面组成：**实时天气**和**未来3天天气**。实时天气界面展示当前天气情况，主要包括天气、温度、湿度和能见度等信息。未来3天天气页面用于展示未来三天的天气情况。
 最终要实现的效果图如下：
 ![实时天气](../../../images/guide/ui-weather-now.png) ![7天预报](../../../images/guide/ui-weather-3d.png)
 ## [#](user-interface.html#准备工作) 准备工作
 这个App使用**和风天气API**获取天气数据，图标使用**和风天气图标**。
 该使用指南中的天气App仅用于演示 Vela JS 应用开发技术，如需在实际项目中使用相关接口和资源，请到和风天气开发平台注册并开通接口后使用。详细信息可在官网查看：
 
- 和风天气开发平台: [https://dev.qweather.com/  (opens new window)](https://dev.qweather.com/)
 - 和风天气图标: [https://icons.qweather.com/  (opens new window)](https://icons.qweather.com/)
 - 图标下载地址: [https://github.com/qwd/WeatherIcon  (opens new window)](https://github.com/qwd/WeatherIcon)

 ## [#](user-interface.html#页面结构) 页面结构
 在[项目结构](project-overview.html)章节中，我们介绍了一个页面(ux文件)包含三部分：`template`、`style`和`script`
。接下来分别编写这两个页面的模板（`template`）代码。
 跟HTML非常类似，Vela的页面模板也是由标签和属性构成，并且语法也大部分与HTML保持一致。不一样的是Vela有自己的一系列内置组件，跟HTML支持的不完全相同。
 接下来的代码中，我们使用到了`div`、`text`和`image`组件，关于组件详细的使用方法，可以参考[Vela官方文档 - 组件](https://iot.mi.com/zh/components)。
 ### [#](user-interface.html#实时天气) 实时天气
 实时天气页面，从整体上可以划分为上中下三部分： `header`、`body`和`footer`，代码如下。
 ```

```
温馨提示
 template只能有一个根节点。

 头部信息，包含城市、省份和国家信息，结构相对简单，代码如下：
 ```

  武汉市
  湖北省/中国

```
温馨提示
 文本必须放在text组件中，否则文本将无法展示在界面中。

 天气信息部分，相比头部要稍微复杂一点，总体可以分为左右两列，右侧部分又分为上下两行。代码如下：
 ```

    6°
    晴转多云

```
接下来实现体感温度等其他信息模块，整体上可以分为三列，每一列又分成两行，代码如下：
 ```

    10°
    体感温度

    67%
    湿度

    5km
    能见度

```
最后是底部更新时间模块，这个模块结构相对比较简单，代码如下：
 ```

  数据更新于12-20 09:15

```
### [#](user-interface.html#未来3天天气) 未来3天天气
 这个页面跟实时天气页面整体结构相同，也分为上中下三部分，并且`header`以及`footer`内容一致，这里不再赘述。
 接下来主要看中间部分的实现，主要包括**未来3天天气概况**以及**天气列表**。
 天气概况分为上下两行，代码如下：
 ```

  未来3天预报
  最高温8° 最低温-6°

```
天气列表相对复杂一点，首先整体可以分为三列，每一列又可分成三行，代码如下：
 ```

    周日
    
    -6°~8°

    周一
    
    -9°~4°

    周二
    
    -3°~6°

```
## [#](user-interface.html#页面样式) 页面样式
 Vela支持常用的CSS特性，也进行了少量的扩充以及修改，默认支持对不同尺寸屏幕的适配。详细的属性支持情况可以在[属性列表](../../components/general/style.html#属性列表)中查询。
 Vela JS 应用采用`flex`布局，可以方便实现常用的布局样式，关于`flex`布局的技术细节，可以参考[MDN文档  (opens new window)](https://developer.mozilla.org/zh-CN/docs/Web/CSS/flex)。
 目前只支持类选择器、ID选择器、分组选择器(,)和标签选择器，不支持后代选择器、属性选择器、通用选择器(*)、兄弟选择器(+)、直接父子选择器(>)、伪类和继承。
 目前为止，我们已经完成了页面结构的代码编写，但是目前为止，还未涉及任何样式的设置。接下来我们将分别实现各个模块的样式。
 ### [#](user-interface.html#公共样式) 公共样式
 编写样式代码之前，可以先提取出一些基础的公共样式，比如排列方式，颜色和对齐方式等。
 在我们这个项目中，主要使用到了水平排列和垂直排列，居中等。
颜色方面，主要是白色，我们可以将text的颜色默认设置为白色。
 提取出的基础样式为：
 ```
/* 公共样式 */
text {
  color: #ffffff;
}

.column {
  flex-direction: column;
}

.row {
  flex-direction: row;
}

.center {
  align-items: center;
  justify-content: center;
}

```
### [#](user-interface.html#page) page
 为了解决屏幕适配问题，所有与大小相关的样式（例如`width`、`font-size`）均以基准宽度（默认`480px`）为基础，根据实际屏幕宽度进行缩放，例如`width:100px`在`960px`宽度屏幕上，实际上为`200px`。
 我们设计稿按照`480px`宽度进行设计，所以设计稿中的尺寸，可以直接在样式中使用。比如下图中，整体宽度为480，体感温度等信息模块的尺寸为`335*100`，那么CSS代码为：
 ```
.info {
  width: 335px;
  height: 100px; 
}

```
![设计稿尺寸](../../../images/guide/ui-figma-size.png)
 对于整体页面，我们先将页面背景设置为黑色，形状设置为圆形，并设置页面宽度。
 ```
.page {
  padding: 40px;
  background-color: #000000;
  width: 480px;
  border-radius: 240px;
}

```
说明
 如果设计稿基准宽度不是480，可以在`manifest.json`文件中通过`config.designWidth`字段配置：

 ```
{
  // ...
  "config": {
    "designWidth": 360
  }
}

```
### [#](user-interface.html#头部信息) 头部信息
 头部信息竖向排列，并且居中，可以使用前面抽取的公共样式：`column`和`center`。然后给文本分别添加样式，控制字体大小和颜色。
 修改后的模板代码如下：
 ```

  武汉市
  湖北省/中国

```
CSS代码如下：
 ```
/* 头部样式 */
.city {
  font-size: 40px;
}

.province {
  font-size: 18px;
  color: #757575;
}

```
实际运行结果：
 ![头部信息运行结果](../../../images/guide/ui-header.png)
 ### [#](user-interface.html#实时天气-2) 实时天气
 跟头部信息类似，首先添加基础样式`column`、`row`和`center`来实现基本的布局，然后对各个文本组件，针对性的编写CSS代码来实现文字颜色，尺寸等样式。
 添加完成后的模板代码如下：
 ```

      6°
      晴转多云

      10°
      体感温度

      67%
      湿度

      5km
      能见度

```
体感温度等信息模块，首先水平方向排列(`flex-direction: row`)，然后各个item设置`flex: 1`，这样就实现了各个item宽度相等的效果。
 具体到每个item里面，只需要分别设置各个文本字段的颜色和大小即可。
 CSS代码如下：
 ```
/* 天气数据样式 */
.body {
  flex: 1;
}

.temp {
  font-size: 70px;
}

.icon {
  width: 170px;
  height: 170px;
  margin-right: 20px;
}

.weather {
  font-size: 24px;
}

/* 更多信息样式 */
.more-info {
  width: 335px;
  height: 100px;
  background-color: rgba(255, 255, 255, 0.21);
  border-radius: 15px;
}

.item {
  flex: 1;
}

.value {
  font-size: 30px;
}

.sub {
  font-size: 14px;
  margin-top: 10px;
}

.label {
  color: #757575;
  margin-top: 5px;
}

```
实际运行结果：
 ![实时天气运行结果](../../../images/guide/ui-now-main.png)
 ### [#](user-interface.html#未来3天天气-2) 未来3天天气
 天气概况信息模块样式比较简单，竖向排列即可。
 跟体感温度等信息模块类似，未来三天天气列表也使用水平方向排列。不同的地方在于，各个item有背景颜色并且之间有间距，所以给各个item设置了固定的尺寸，然后根据剩下的空间
给各个item之间分配间距(`justify-content: space-between`)。
 修改完成后的模板代码为：
 ```

    周日
    
    -6°~8°

    周一
    
    -9°~4°

    周二
    
    -3°~6°

```
CSS代码为：
 ```
/* 天气数据样式 */
.body {
  flex: 1;
}

.info {
  margin-bottom: 20px;
}

.title {
  font-size: 30px;
}

.summary {
  font-size: 24px;
  color: #757575;
}

/* 未来3天天气 */
.list {
  width: 380px;
  justify-content: space-between;
}

.item {
  width: 120px;
  height: 175px;
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 15px;
}

.date {
  font-size: 28px;
}

.icon {
  width: 90px;
  height: 90px;
}

.temp {
  font-size: 24px;
}

```
实际运行结果：
 ![实时天气运行结果](../../../images/guide/ui-3d-list.png)
 ### [#](user-interface.html#底部信息) 底部信息
 底部信息最终的模板代码如下：
 ```

  数据更新于 12-20 09:15

```
CSS代码如下：
 ```
/* 底部样式 */
.footer {
  margin-top: 20px;    
}

.update-time {
  color: #757575;
}

```
实际运行结果：
 ![实时天气运行结果](../../../images/guide/ui-footer.png)
 ## [#](user-interface.html#页面效果) 页面效果
 完成页面结构和样式后，模拟器中实际运行的结果如下：
 ![实时天气运行结果](../../../images/guide/ui-now-result.png) ![未来3天天气运行结果](../../../images/guide/ui-3d-result.png)
 ## [#](user-interface.html#页面数据) 页面数据
 我们已经实现了页面数据的渲染，但是目前所有的数据都是直接写到模板代码中，不能在程序中动态修改。
 如果需要在程序中动态修改界面上展示的数据，需要将数据存储到[页面数据对象](../framework/script/page-data.html)中，然后使用双大括号语法来引用数据，比如`{{ name }}`，详细使用方法可以参考[模板语法](../framework/template/index.html)。
 数据定义，需要通过JavaScript脚本来实现。跟HTML一样，JavaScript代码需要放在`script`标签中：
 ```

  export default {
    private: {
      city: '武汉市',
      province: '湖北省',
      country: '中国',
      weather: {
        // 数据观测时间
        obsTime: "12-21 09:05",
        // 温度
        temp: "13",
        // 体感温度
        feelsLike: "10",
        // 天气icon图标编号
        icon: "101",
        // 天气描述文本
        text: "多云",
        // 相对湿度
        humidity: "72",
        // 能见度，单位：公里
        vis: "16"
      }
    }
  }

```
定义好数据之后，替换之前的模板代码，替换后为：
 ```

      {{city}}
      {{province}}/{{country}}

          {{weather.temp}}°
          {{weather.text}}

          {{weather.feelsLike}}°
          体感温度

          {{weather.humidity}}%
          湿度

          {{weather.vis}}km
          能见度

      数据更新于 {{weather.obsTime}}

```
## [#](user-interface.html#列表渲染) 列表渲染
 在未来3天天气预报页面中，使用到了数组存储未来3天的天气数据。可以使用`for`指令来渲染（详细的`for`指令用法，请参考[列表渲染](../framework/template/for.html)）。
 数据定义为：
 ```
{
  list: [
    {
      "fxDate": "周日",
      "tempMax": "12",
      "tempMin": "-1",
      "iconDay": "101",
      "textDay": "多云",
    },
    {
      "fxDate": "周一",
      "tempMax": "13",
      "tempMin": "0",
      "iconDay": "100",
      "textDay": "晴"
    },
    {
      "fxDate": "周二",
      "tempMax": "13",
      "tempMin": "0",
      "iconDay": "302",
      "textDay": "晴",
      "iconNight": "150",
      "textNight": "晴"
    }
  ]
}

```
模板代码：
 ```

    {{$item.fxDate}}
    
    {{$item.tempMin}}°~{{$item.tempMax}}°

```
运行结果：
 ![列表渲染结果](../../../images/guide/ui-for-list-result.png)
 ### [#](user-interface.html#条件渲染) 条件渲染
 实际项目中经常会需要使用到条件判断，根据不同的条件渲染不同的UI。要使用条件渲染，请参考[条件指令](../framework/template/if.html)。


---
## 文件: guide\version\APILevel2.html

# # 概述

# [#](APILevel4.html#概述) 概述
 Xiaomi Vela JS 应用是小米公司开发的一种应用类型，它是基于小米的物联网嵌入式软件平台 Xiaomi Vela OS 开发的。本文将介绍 Xiaomi Vela JS 应用的特点、应用场景以及开发支持，帮助开发者更好地了解和使用这一应用类型。
 如果您想快速了解如何开发 Xiaomi Vela JS 应用，并且希望快速上手，请直接访问 [快速入门](../start.html) 章节。
 ## [#](APILevel4.html#应用特点) 应用特点
 Xiaomi Vela JS 应用是一种基于 Xiaomi Vela OS 操作系统的轻量级应用模式，旨在为智能穿戴设备提供更加流畅和便捷的用户体验。它具备以下显著特点：
 
- **轻量化：**
Xiaomi Vela JS 应用采用了轻量级的架构设计，与传统的应用程序相比，Xiaomi Vela JS 应用具有更小的体积，这使得它们能够快速加载和运行，尤其适合内存和处理能力有限的穿戴设备。
 - **跨平台兼容性：**
Xiaomi Vela JS 应用支持跨端运行，开发者可以一次开发，实现在多种设备上的运行，这大大提高了开发效率和应用的普及率。
 - **高性能渲染：**
系统优化了渲染能力，使得应用的动画和交互更为流畅，提升了用户的使用体验。
 - **安全性能：**
Xiaomi Vela OS 通过三重隔离机制确保了应用的安全性，保护了用户数据和设备的安全。
 - **开发支持：**
小米提供了全面的开发支持 Xiaomi Vela JS 应用的开发工具和文档齐全，开发者可以轻松上手，快速构建高质量的应用。小米提供了包括AIoT-IDE在内的一系列开发工具，支持开发者在Ubuntu、Windows、MacOS等操作系统上进行Xiaomi Vela JS 应用的开发和调试。

 ## [#](APILevel4.html#应用场景) 应用场景
 Xiaomi Vela JS 应用的应用场景广泛，已落地多款产品，覆盖了智能穿戴设备上的多种使用情形：
 
- **健康监测：**  应用可以实时监测用户的心率、睡眠质量等健康数据，为用户提供健康建议和预警。
 - **运动辅助：**  在用户进行运动时，应用能够记录运动数据，提供运动指导和健康管理。
 - **消息提醒：**  应用能够显示手机等设备的消息提醒，方便用户在不拿出手机的情况下查看重要信息。
 - **移动支付：**  应用可以集成支付功能，用户可以直接在穿戴设备上完成支付操作，提高支付的便捷性。
 - **智能控制：**  作为智能家居的控制中心，应用可以远程操控家中的智能设备，如灯光、空调等。
 - **日常工具：**  提供天气预报、闹钟、计时器等日常工具功能，满足用户的多样化需求。

 ## [#](APILevel4.html#技术优势) 技术优势
 相较于传统的应用框架，Xiaomi Vela JS 应用具有以下技术优势：
 ### [#](APILevel4.html#前端开发范式) 前端开发范式
 Xiaomi Vela JS 应用采用JavaScript语言开发，并且支持前端MVVM高效的开发范式，响应式UI框架，易学易用。使得开发者可以降低上手难度，缩短开发周期。这种模式贴合主流前端开发者的思维习惯，使得开发者能够快速构建出功能丰富、交互友好的应用，同时降低了学习成本。参考[开发语法](../framework/index.html)。
 ### [#](APILevel4.html#统一的api和组件) 统一的API和组件
 Xiaomi Vela JS 应用提供了统一的[JS接口](../../features/index.html)和[UI组件](../../components/index.html)，使得开发者无需关心底层硬件和操作系统的差异，简化了开发流程，同时保证了应用的质量和用户体验。
 ### [#](APILevel4.html#高性能渲染) 高性能渲染
 
- 通过架构优化，让复杂计算下沉到原生层，解决 JS 语言的性能瓶颈，从而拥有媲美原生的运行效率和流畅体验。
 - 提供丰富的动效能力，包括30+插值和物理动效，可用于过渡、转场等动画效果，使用户界面更加生动、有趣和富有表现力。
 - 充分挖掘硬件性能，最大限度利用 GPU 和 CPU 的硬件加速能力，让复杂UI 界面和动画更加流畅，达到 60 fps 满帧效果。

 ### [#](APILevel4.html#多屏适配) 多屏适配
 [多屏适配](../multi-screens/index.html)是Xiaomi Vela JS 应用框架的另一大特色，具体表现在：
 
- **[适配规范](../multi-screens/specs.html)：**  框架支持不同形状、尺寸和分辨率的屏幕自适应，确保应用在各种设备上都能提供良好的视觉体验。
 - **[设计规范](../design/multi-screens.html)：**  Vela提供了一套多屏设计的技术规范，帮助开发者按照设计稿完成应用的多屏适配。
 - **[多屏UI模拟器](../multi-screens/simulator.html)：**  AIoT-IDE提供了多屏UI模拟器，使开发者能够快速预览应用在不同屏幕上的效果，进行必要的调整。
 - **[适配案例](../multi-screens/samples.html)：**  提供了对常见页面元素进行多屏适配的代码示例以及整站案例，供开发者参考和学习。

 ## [#](APILevel4.html#应用开发流程) 应用开发流程
 ### [#](APILevel4.html#环境搭建) 环境搭建
 小米提供了包括AIoT-IDE在内的一系列开发工具，支持开发者在Ubuntu、Windows、MacOS等操作系统上进行Xiaomi Vela JS 应用的开发和调试。
AIoT-IDE是Xiaomi Vela JS 应用的集成开发环境，提供了项目初始化、编码、调试等一系列功能。请参考[安装环境](../start/use-ide.html)初始化项目。
 ### [#](APILevel4.html#应用开发) 应用开发
 初始化项目后请参考[项目结构](../start/project-overview.html)了解项目中各文件和目录的作用。项目由配置文件（manifest.json）、模板代码（ux文件）、 样式代码（css文件）、逻辑代码（js文件）以及资源文件（图片、音频等）组成。请参考[项目配置](../framework/manifest.html)对项目相关信息进行配置。
 应用开发范式遵循声明式UI，和传统Web开发范式类似。项目中的页面以及组件均由`ux`后缀文件编写，文件由[template 模板](../framework/template/index.html)、[style 样式](../framework/style/index.html)和[script 脚本](../framework/script/index.html)3 个部分组成。开发者可以通过编写`ux`文件使用[UI组件](../../components/index.html)、[自定义组件](../framework/template/component.html)和样式描述页面结构和呈现效果，使用js脚本定义页面数据、实现生命周期接口、通用方法、事件处理等。
请参考[编写页面UI](../start/user-interface.html)进一步了解页面开发。
 ### [#](APILevel4.html#运行调试) 运行调试
 AIoT-IDE提供内置的模拟器，支持开发者启动模拟器在IDE中直接运行和调试应用，实时查看运行效果。请参考[运行调试](../start/use-ide.html#_5-运行项目.html)了解如何运行和调试应用。
 ### [#](APILevel4.html#打包发布) 打包发布
 应用开发完成后，开发者可以使用AIoT-IDE提供的打包功能将应用打包成安装包，请参考[打包项目](../start/use-ide.html#_7-打包项目.html)了解如何打包应用。项目打包成功后请参考[发布](../publish/index.html)进行应用发布。


---
## 文件: guide\version\APILevel3.html

# # 概述

# [#](APILevel4.html#概述) 概述
 Xiaomi Vela JS 应用是小米公司开发的一种应用类型，它是基于小米的物联网嵌入式软件平台 Xiaomi Vela OS 开发的。本文将介绍 Xiaomi Vela JS 应用的特点、应用场景以及开发支持，帮助开发者更好地了解和使用这一应用类型。
 如果您想快速了解如何开发 Xiaomi Vela JS 应用，并且希望快速上手，请直接访问 [快速入门](../start.html) 章节。
 ## [#](APILevel4.html#应用特点) 应用特点
 Xiaomi Vela JS 应用是一种基于 Xiaomi Vela OS 操作系统的轻量级应用模式，旨在为智能穿戴设备提供更加流畅和便捷的用户体验。它具备以下显著特点：
 
- **轻量化：**
Xiaomi Vela JS 应用采用了轻量级的架构设计，与传统的应用程序相比，Xiaomi Vela JS 应用具有更小的体积，这使得它们能够快速加载和运行，尤其适合内存和处理能力有限的穿戴设备。
 - **跨平台兼容性：**
Xiaomi Vela JS 应用支持跨端运行，开发者可以一次开发，实现在多种设备上的运行，这大大提高了开发效率和应用的普及率。
 - **高性能渲染：**
系统优化了渲染能力，使得应用的动画和交互更为流畅，提升了用户的使用体验。
 - **安全性能：**
Xiaomi Vela OS 通过三重隔离机制确保了应用的安全性，保护了用户数据和设备的安全。
 - **开发支持：**
小米提供了全面的开发支持 Xiaomi Vela JS 应用的开发工具和文档齐全，开发者可以轻松上手，快速构建高质量的应用。小米提供了包括AIoT-IDE在内的一系列开发工具，支持开发者在Ubuntu、Windows、MacOS等操作系统上进行Xiaomi Vela JS 应用的开发和调试。

 ## [#](APILevel4.html#应用场景) 应用场景
 Xiaomi Vela JS 应用的应用场景广泛，已落地多款产品，覆盖了智能穿戴设备上的多种使用情形：
 
- **健康监测：**  应用可以实时监测用户的心率、睡眠质量等健康数据，为用户提供健康建议和预警。
 - **运动辅助：**  在用户进行运动时，应用能够记录运动数据，提供运动指导和健康管理。
 - **消息提醒：**  应用能够显示手机等设备的消息提醒，方便用户在不拿出手机的情况下查看重要信息。
 - **移动支付：**  应用可以集成支付功能，用户可以直接在穿戴设备上完成支付操作，提高支付的便捷性。
 - **智能控制：**  作为智能家居的控制中心，应用可以远程操控家中的智能设备，如灯光、空调等。
 - **日常工具：**  提供天气预报、闹钟、计时器等日常工具功能，满足用户的多样化需求。

 ## [#](APILevel4.html#技术优势) 技术优势
 相较于传统的应用框架，Xiaomi Vela JS 应用具有以下技术优势：
 ### [#](APILevel4.html#前端开发范式) 前端开发范式
 Xiaomi Vela JS 应用采用JavaScript语言开发，并且支持前端MVVM高效的开发范式，响应式UI框架，易学易用。使得开发者可以降低上手难度，缩短开发周期。这种模式贴合主流前端开发者的思维习惯，使得开发者能够快速构建出功能丰富、交互友好的应用，同时降低了学习成本。参考[开发语法](../framework/index.html)。
 ### [#](APILevel4.html#统一的api和组件) 统一的API和组件
 Xiaomi Vela JS 应用提供了统一的[JS接口](../../features/index.html)和[UI组件](../../components/index.html)，使得开发者无需关心底层硬件和操作系统的差异，简化了开发流程，同时保证了应用的质量和用户体验。
 ### [#](APILevel4.html#高性能渲染) 高性能渲染
 
- 通过架构优化，让复杂计算下沉到原生层，解决 JS 语言的性能瓶颈，从而拥有媲美原生的运行效率和流畅体验。
 - 提供丰富的动效能力，包括30+插值和物理动效，可用于过渡、转场等动画效果，使用户界面更加生动、有趣和富有表现力。
 - 充分挖掘硬件性能，最大限度利用 GPU 和 CPU 的硬件加速能力，让复杂UI 界面和动画更加流畅，达到 60 fps 满帧效果。

 ### [#](APILevel4.html#多屏适配) 多屏适配
 [多屏适配](../multi-screens/index.html)是Xiaomi Vela JS 应用框架的另一大特色，具体表现在：
 
- **[适配规范](../multi-screens/specs.html)：**  框架支持不同形状、尺寸和分辨率的屏幕自适应，确保应用在各种设备上都能提供良好的视觉体验。
 - **[设计规范](../design/multi-screens.html)：**  Vela提供了一套多屏设计的技术规范，帮助开发者按照设计稿完成应用的多屏适配。
 - **[多屏UI模拟器](../multi-screens/simulator.html)：**  AIoT-IDE提供了多屏UI模拟器，使开发者能够快速预览应用在不同屏幕上的效果，进行必要的调整。
 - **[适配案例](../multi-screens/samples.html)：**  提供了对常见页面元素进行多屏适配的代码示例以及整站案例，供开发者参考和学习。

 ## [#](APILevel4.html#应用开发流程) 应用开发流程
 ### [#](APILevel4.html#环境搭建) 环境搭建
 小米提供了包括AIoT-IDE在内的一系列开发工具，支持开发者在Ubuntu、Windows、MacOS等操作系统上进行Xiaomi Vela JS 应用的开发和调试。
AIoT-IDE是Xiaomi Vela JS 应用的集成开发环境，提供了项目初始化、编码、调试等一系列功能。请参考[安装环境](../start/use-ide.html)初始化项目。
 ### [#](APILevel4.html#应用开发) 应用开发
 初始化项目后请参考[项目结构](../start/project-overview.html)了解项目中各文件和目录的作用。项目由配置文件（manifest.json）、模板代码（ux文件）、 样式代码（css文件）、逻辑代码（js文件）以及资源文件（图片、音频等）组成。请参考[项目配置](../framework/manifest.html)对项目相关信息进行配置。
 应用开发范式遵循声明式UI，和传统Web开发范式类似。项目中的页面以及组件均由`ux`后缀文件编写，文件由[template 模板](../framework/template/index.html)、[style 样式](../framework/style/index.html)和[script 脚本](../framework/script/index.html)3 个部分组成。开发者可以通过编写`ux`文件使用[UI组件](../../components/index.html)、[自定义组件](../framework/template/component.html)和样式描述页面结构和呈现效果，使用js脚本定义页面数据、实现生命周期接口、通用方法、事件处理等。
请参考[编写页面UI](../start/user-interface.html)进一步了解页面开发。
 ### [#](APILevel4.html#运行调试) 运行调试
 AIoT-IDE提供内置的模拟器，支持开发者启动模拟器在IDE中直接运行和调试应用，实时查看运行效果。请参考[运行调试](../start/use-ide.html#_5-运行项目.html)了解如何运行和调试应用。
 ### [#](APILevel4.html#打包发布) 打包发布
 应用开发完成后，开发者可以使用AIoT-IDE提供的打包功能将应用打包成安装包，请参考[打包项目](../start/use-ide.html#_7-打包项目.html)了解如何打包应用。项目打包成功后请参考[发布](../publish/index.html)进行应用发布。


---
## 文件: guide\version\APILevel4.html

# # 概述

# [#](APILevel4.html#概述) 概述
 Xiaomi Vela JS 应用是小米公司开发的一种应用类型，它是基于小米的物联网嵌入式软件平台 Xiaomi Vela OS 开发的。本文将介绍 Xiaomi Vela JS 应用的特点、应用场景以及开发支持，帮助开发者更好地了解和使用这一应用类型。
 如果您想快速了解如何开发 Xiaomi Vela JS 应用，并且希望快速上手，请直接访问 [快速入门](../start.html) 章节。
 ## [#](APILevel4.html#应用特点) 应用特点
 Xiaomi Vela JS 应用是一种基于 Xiaomi Vela OS 操作系统的轻量级应用模式，旨在为智能穿戴设备提供更加流畅和便捷的用户体验。它具备以下显著特点：
 
- **轻量化：**
Xiaomi Vela JS 应用采用了轻量级的架构设计，与传统的应用程序相比，Xiaomi Vela JS 应用具有更小的体积，这使得它们能够快速加载和运行，尤其适合内存和处理能力有限的穿戴设备。
 - **跨平台兼容性：**
Xiaomi Vela JS 应用支持跨端运行，开发者可以一次开发，实现在多种设备上的运行，这大大提高了开发效率和应用的普及率。
 - **高性能渲染：**
系统优化了渲染能力，使得应用的动画和交互更为流畅，提升了用户的使用体验。
 - **安全性能：**
Xiaomi Vela OS 通过三重隔离机制确保了应用的安全性，保护了用户数据和设备的安全。
 - **开发支持：**
小米提供了全面的开发支持 Xiaomi Vela JS 应用的开发工具和文档齐全，开发者可以轻松上手，快速构建高质量的应用。小米提供了包括AIoT-IDE在内的一系列开发工具，支持开发者在Ubuntu、Windows、MacOS等操作系统上进行Xiaomi Vela JS 应用的开发和调试。

 ## [#](APILevel4.html#应用场景) 应用场景
 Xiaomi Vela JS 应用的应用场景广泛，已落地多款产品，覆盖了智能穿戴设备上的多种使用情形：
 
- **健康监测：**  应用可以实时监测用户的心率、睡眠质量等健康数据，为用户提供健康建议和预警。
 - **运动辅助：**  在用户进行运动时，应用能够记录运动数据，提供运动指导和健康管理。
 - **消息提醒：**  应用能够显示手机等设备的消息提醒，方便用户在不拿出手机的情况下查看重要信息。
 - **移动支付：**  应用可以集成支付功能，用户可以直接在穿戴设备上完成支付操作，提高支付的便捷性。
 - **智能控制：**  作为智能家居的控制中心，应用可以远程操控家中的智能设备，如灯光、空调等。
 - **日常工具：**  提供天气预报、闹钟、计时器等日常工具功能，满足用户的多样化需求。

 ## [#](APILevel4.html#技术优势) 技术优势
 相较于传统的应用框架，Xiaomi Vela JS 应用具有以下技术优势：
 ### [#](APILevel4.html#前端开发范式) 前端开发范式
 Xiaomi Vela JS 应用采用JavaScript语言开发，并且支持前端MVVM高效的开发范式，响应式UI框架，易学易用。使得开发者可以降低上手难度，缩短开发周期。这种模式贴合主流前端开发者的思维习惯，使得开发者能够快速构建出功能丰富、交互友好的应用，同时降低了学习成本。参考[开发语法](../framework/index.html)。
 ### [#](APILevel4.html#统一的api和组件) 统一的API和组件
 Xiaomi Vela JS 应用提供了统一的[JS接口](../../features/index.html)和[UI组件](../../components/index.html)，使得开发者无需关心底层硬件和操作系统的差异，简化了开发流程，同时保证了应用的质量和用户体验。
 ### [#](APILevel4.html#高性能渲染) 高性能渲染
 
- 通过架构优化，让复杂计算下沉到原生层，解决 JS 语言的性能瓶颈，从而拥有媲美原生的运行效率和流畅体验。
 - 提供丰富的动效能力，包括30+插值和物理动效，可用于过渡、转场等动画效果，使用户界面更加生动、有趣和富有表现力。
 - 充分挖掘硬件性能，最大限度利用 GPU 和 CPU 的硬件加速能力，让复杂UI 界面和动画更加流畅，达到 60 fps 满帧效果。

 ### [#](APILevel4.html#多屏适配) 多屏适配
 [多屏适配](../multi-screens/index.html)是Xiaomi Vela JS 应用框架的另一大特色，具体表现在：
 
- **[适配规范](../multi-screens/specs.html)：**  框架支持不同形状、尺寸和分辨率的屏幕自适应，确保应用在各种设备上都能提供良好的视觉体验。
 - **[设计规范](../design/multi-screens.html)：**  Vela提供了一套多屏设计的技术规范，帮助开发者按照设计稿完成应用的多屏适配。
 - **[多屏UI模拟器](../multi-screens/simulator.html)：**  AIoT-IDE提供了多屏UI模拟器，使开发者能够快速预览应用在不同屏幕上的效果，进行必要的调整。
 - **[适配案例](../multi-screens/samples.html)：**  提供了对常见页面元素进行多屏适配的代码示例以及整站案例，供开发者参考和学习。

 ## [#](APILevel4.html#应用开发流程) 应用开发流程
 ### [#](APILevel4.html#环境搭建) 环境搭建
 小米提供了包括AIoT-IDE在内的一系列开发工具，支持开发者在Ubuntu、Windows、MacOS等操作系统上进行Xiaomi Vela JS 应用的开发和调试。
AIoT-IDE是Xiaomi Vela JS 应用的集成开发环境，提供了项目初始化、编码、调试等一系列功能。请参考[安装环境](../start/use-ide.html)初始化项目。
 ### [#](APILevel4.html#应用开发) 应用开发
 初始化项目后请参考[项目结构](../start/project-overview.html)了解项目中各文件和目录的作用。项目由配置文件（manifest.json）、模板代码（ux文件）、 样式代码（css文件）、逻辑代码（js文件）以及资源文件（图片、音频等）组成。请参考[项目配置](../framework/manifest.html)对项目相关信息进行配置。
 应用开发范式遵循声明式UI，和传统Web开发范式类似。项目中的页面以及组件均由`ux`后缀文件编写，文件由[template 模板](../framework/template/index.html)、[style 样式](../framework/style/index.html)和[script 脚本](../framework/script/index.html)3 个部分组成。开发者可以通过编写`ux`文件使用[UI组件](../../components/index.html)、[自定义组件](../framework/template/component.html)和样式描述页面结构和呈现效果，使用js脚本定义页面数据、实现生命周期接口、通用方法、事件处理等。
请参考[编写页面UI](../start/user-interface.html)进一步了解页面开发。
 ### [#](APILevel4.html#运行调试) 运行调试
 AIoT-IDE提供内置的模拟器，支持开发者启动模拟器在IDE中直接运行和调试应用，实时查看运行效果。请参考[运行调试](../start/use-ide.html#_5-运行项目.html)了解如何运行和调试应用。
 ### [#](APILevel4.html#打包发布) 打包发布
 应用开发完成后，开发者可以使用AIoT-IDE提供的打包功能将应用打包成安装包，请参考[打包项目](../start/use-ide.html#_7-打包项目.html)了解如何打包应用。项目打包成功后请参考[发布](../publish/index.html)进行应用发布。


---
## 文件: guide\version\index.html

# # APILevel

# [#](index.html#apilevel) APILevel
 APILevel 用来标识当前框架提供的接口标准集合，文档中所有接口是默认属于 APILevel 1，如果
 某些接口属于其他版本，会被标注出来。


---
## 文件: samples\index.html

![封面](../../images/sample/calendar.png)
 日历 日历应用是一种方便用户记录、安排和跟踪重要事件和活动的工具。该应用已实现多屏适配，可查看当前年份日期，点击日期可查看更详细的日期信息。应用页面设计简单，操作友好，基于此应用能快速上手开发出一个功能更丰富的日历应用。 [![](../../images/github.svg)](https://github.com/open-vela/packages_apps/tree/dev/wearable/calendar) [![](../../images/gitee.svg)](https://gitee.com/open-vela/packages_apps/tree/dev/wearable/calendar)

![封面](../../images/sample/chart.png)
 图表 图表快应用是一款数据可视化工具，能够快速、简单地基于数据生成各种类型的图表，包括折线图、柱状图等多种常见图表类型。该应用已实现多屏适配，简单易用，操作方便，适用于各种数据分析和展示场景。 [![](../../images/github.svg)](https://github.com/open-vela/packages_apps/tree/dev/wearable/chart) [![](../../images/gitee.svg)](https://gitee.com/open-vela/packages_apps/tree/dev/wearable/chart)

![封面](../../images/sample/multi-screen-calculator.png)
 计算器UI 计算器快应用UI已适配方屏、圆屏、跑道屏，基于不同屏幕展示不同的UI样式，同时是一款功能强大、易于使用的计算器应用程序。基于此示例开发能够执行各种数学运算，包括基本的加、减、乘、除、百分比等运算。 [![](../../images/github.svg)](https://github.com/open-vela/packages_apps/tree/dev/wearable/multi-screen-calculator) [![](../../images/gitee.svg)](https://gitee.com/open-vela/packages_apps/tree/dev/wearable/multi-screen-calculator)

![封面](../../images/sample/multi_screen_todolist.png)
 任务清单 任务清单快应用是一款简单易用的任务管理工具，可以帮助你高效地记录和管理日常的任务和待办事项。该应用已适配方屏、圆屏、跑道屏，页面设计简单，操作友好，基于此应用能快速上手开发出一个功能更丰富的任务管理工具。 [![](../../images/github.svg)](https://github.com/open-vela/packages_apps/tree/dev/wearable/multi_screen_todolist) [![](../../images/gitee.svg)](https://gitee.com/open-vela/packages_apps/tree/dev/wearable/multi_screen_todolist)

![封面](../../images/sample/player.png)
 播放器 这是一个高效、易于使用的播放器快应用。该应用具有简单易用的界面，并且已适配圆屏、方屏等多屏幕，功能包括歌曲播放暂停、切换上下首，展示播放列表。播放器页面设计友好、交互简单，基于此示例能够快速上手开发出一个功能更丰富的播放器快应用。 [![](../../images/github.svg)](https://github.com/open-vela/packages_apps/tree/dev/wearable/player) [![](../../images/gitee.svg)](https://gitee.com/open-vela/packages_apps/tree/dev/wearable/player)

![封面](../../images/sample/settings.png)
 设置UI 这是一个设置应用UI并已支持多屏适配，基于此开发可以让用户轻松地访问和更改各种设置，包括网络设置、音量、蓝牙、Wi-Fi、屏幕亮度、通知等等。该应用具有简单易用的界面，使用户可以轻松地找到他们需要的设置选项。 [![](../../images/github.svg)](https://github.com/open-vela/packages_apps/tree/dev/wearable/settings) [![](../../images/gitee.svg)](https://gitee.com/open-vela/packages_apps/tree/dev/wearable/settings)

![封面](../../images/sample/input_method.png)
 输入法 输入法应用是一种非常实用的工具，可以帮助用户在手表手环上快速、准确地输入文字，提高工作和学习的效率。该输入法组件已适配圆屏、方屏等多屏幕，并且支持中英文切换。通过引入该组件可以轻松实现在手表手环上输入文字。 [![](../../images/github.svg)](https://github.com/NEORUAA/Vela_input_method)  

![封面](../../images/sample/publish_subscribe.png)
 订阅发布 这款 Demo 基于经典的订阅 - 发布（Pub/Sub）设计模式，提供轻量、灵活的跨模块通信方案，无需模块间直接依赖即可实现消息传递。核心功能包括事件订阅（$on）、消息发布（$emit）、订阅取消（$off）及事件存在判断（$judge），支持多场景下的异步通信需求。无论是前端组件间协作、插件模块联动，都能通过简单调用实现高效消息传递，帮助简化代码依赖关系、提升项目可维护性。代码可直接集成到Vela快应用项目中，是学习设计模式、解决跨模块通信问题的实用参考。 [![](../../images/github.svg)](https://github.com/open-vela/packages_apps/tree/dev/wearable/eventBus) [![](../../images/gitee.svg)](https://gitee.com/open-vela/packages_apps/tree/dev/wearable/eventBus)


---
## 文件: tools\index.html

# # 关于AIoT-IDE

# [#](index.html#关于aiot-ide) 关于AIoT-IDE
 `AIoT-IDE` 是用于开发`Xiaomi Vela JS 应用`的官方集成开发环境。建立在 [Visual Studio Code  (opens new window)](https://code.visualstudio.com/)（以下简称 VS Code）的基础上，它继承了 VS Code 的全部功能，比如**代码编辑**、**插件集成**、**主题定制**及个性化设置。此外，`AIoT-IDE` 还引入了一系列专门针对 `Xiaomi Vela JS 应用` 应用开发的增强功能，它们包括但不限于：
 
- 智能编码提示
 - Vela JS应用调试
 - 实时编译预览
 - Vela JS应用 打包发布
 - Vela JS应用 真机调试

 本章主要介绍 `AIoT-IDE` 的核心功能。如需下载安装请查看，请直接访问 [安装环境](../guide/start/use-ide.html) 章节。
 ## [#](index.html#项目结构) 项目结构
 只有当`AIoT-IDE`打开`Xiaomi Vela JS 应用`，才能使用应用开发的增强功能。
 一个最基本的`Xiaomi Vela JS 应用`由描述项目信息配置文件`mainfest.json`和放置项目公交资源的`app.ux`文件，不同描述页面的ux文件。
 ```
├── manifest.json
├── app.ux
├── pages
│   ├── index
|   |   └── index.ux
│   └── detail
|       └── detail.ux
├── i18n
|   ├── defaults.json
|   ├── zh-CN.json
|   └── en-US.json
└── common
    ├── style.css
    ├── utils.js
    └── logo.png

```
当`AIoT-IDE`打开一个项目时会判断当前项目的**根目录**或者**src目录**下是否有`mainfest.json`文件，如果有则会读取`mainfest.json`中的**deviceTypeList**，通过**deviceTypeList**字段的内容判断当前项目是什么类型的`Xiaomi Vela JS应用`。


---
## 文件: tools\debug\audit.html

# # 优化评分

# [#](audit.html#优化评分) 优化评分
 为了优化开发者开发体验，`AIoT-IDE`中内置了生成优化评分报告功能，点击`生成报告`按钮即可根据当前打开的应用生成优化报告。
 ![alt text](../../../images/tools/ide-debug-14.png)
 整体分为动态分析和静态分析2部分，每个检测项会包括：触发条目的具体信息（例如代码位置/文件url/网络url），优化建议等。
 ## [#](audit.html#文件分析) 文件分析
 
1. 用来帮助用户比较直观地了解当前包体积占比过高的文件，优化包体积

 1. 后续也规划提供类似treeMap的依赖占比可视化分析

 ![alt text](../../../images/tools/ide-debug-15.png)
 ## [#](audit.html#性能指标) 性能指标
 优化报告中有给出多维度的性能指标，给出优化建议,以便开发者进行优化。
 ![alt text](../../../images/tools/ide-debug-16.png)
 ## [#](audit.html#优化检测项) 优化检测项
 
- 一个统一的、汇总式地检测入口，目前共计上线9则检查项：

- 多次引用代码检测
 - 大型依赖替换检测
 - 未使用依赖检测
 - 未使用系统功能检测
 - 网络请求耗时检测
 - 网络请求https使用检测
 - 网络请求次数检测
 - 网络请求异常检测
 - 代码执行报错检测

 ### [#](audit.html#多次引用代码检测) 多次引用代码检测
 优化报告中会标出多次引用的代码，给出优化建议，以便开发者进行优化。
 ![alt text](../../../images/tools/ide-debug-17.png)
 ### [#](audit.html#大型依赖替换检测) 大型依赖替换检测
 优化报告中会标出体积过大的依赖项，给出优化建议，以便开发者进行优化。
 ![alt text](../../../images/tools/ide-debug-18.png)
 ### [#](audit.html#未使用依赖检测) 未使用依赖检测
 优化报告中会标出未使用的依赖项，给出优化建议，以便开发者进行优化。
 ![alt text](../../../images/tools/ide-debug-19.png)
 ### [#](audit.html#未使用系统功能检测) 未使用系统功能检测
 优化报告中会标出未使用的系统功能，给出优化建议，以便开发者进行优化。
 ![alt text](../../../images/tools/ide-debug-20.png)
 ### [#](audit.html#网络请求耗时检测) 网络请求耗时检测
 优化报告中会标出请求响应耗时过长的请求，给出优化建议，以便开发者进行优化。
 ![alt text](../../../images/tools/ide-debug-21.png)
 ### [#](audit.html#网络请求https使用检测) 网络请求https使用检测
 优化报告中会标出请求响应中未使用https的，给出优化建议，以便开发者进行优化。
 ![alt text](../../../images/tools/ide-debug-22.png)
 ### [#](audit.html#网络请求次数检测) 网络请求次数检测
 优化报告中会标出请求响应中频繁请求的，给出优化建议，以便开发者进行优化。
 ![alt text](../../../images/tools/ide-debug-23.png)
 ### [#](audit.html#网络请求异常检测) 网络请求异常检测
 优化报告中会标出请求响应异常，给出优化建议，以便开发者进行优化。
 ![alt text](../../../images/tools/ide-debug-24.png)
 ### [#](audit.html#代码执行报错检测) 代码执行报错检测
 优化报告中会标出项目中代码执行报错的，给出优化建议，以便开发者进行优化。
 ![alt text](../../../images/tools/ide-debug-25.png)


---
## 文件: tools\debug\build-setting.html

# # 编译设置

# [#](build-setting.html#编译设置) 编译设置
 编译设置功能可自定义模拟器运行时打开的页面和页面缓存,未进行设置时会默认读取项目中的`manifest.json`中的router配置
 ![alt text](../../../images/tools/ide-buildSeting-3.png)
 在顶部banner区域，增加了当前编译模式显示按钮，点击当前编译配置路由路径，可切换编译配置或点击新增添加新的编译配置
 ![alt text](../../../images/tools/ide-buildSeting-1.png)
 点击当前编译配置名称，可进入编译模式设置界面，选择对当前项目的编辑配置进行增删改。
 ![alt text](../../../images/tools/ide-buildSeting-2.png)


---
## 文件: tools\debug\debug.html

# # 调试运行

# [#](debug.html#调试运行) 调试运行
 点击`选择设备`按钮，选择要运行模拟器，点击确定，再点击`调试`按钮将启动对应模拟器。
 ![alt text](../../../images/tools/ide-debug-1.png)
 模拟器启动成功后，将打开调试页面，当运行多个模拟器时。调试窗口对应的是(**如下图标签1，2，3**所示)模拟器运行页面中**标题有选中效果**的模拟器
 ![alt text](../../../images/tools/ide-debug-2.png)
 点击模拟器，可切换调试服务，进行对应调试。
 ![alt text](../../../images/tools/ide-debug-3.png)
 ![alt text](../../../images/tools/ide-debug-4.png)


---
## 文件: tools\debug\memory.html

# # 内存分析

# [#](memory.html#内存分析) 内存分析
 进行**内存泄漏**排查时，您可以通过两次内存快照(dump)来协助分析。例如排查页面内存泄漏，先在进入页面前 dump 一次，再在推出页面后 dump 一次。排查内存泄漏有两种场景：
 对于不依赖底层能力的应用：如果您的应用不需要诸如底层能力，您可以直接在 `AIoT-IDE` 中测试。在问题场景的前后分别点击 位置 4 来进行内存快照。
 对于依赖底层能力的应用：您需要安装可以执行 js 堆内存快照的固件，运行命令 dump_js_heap /sdcard，然后将快照文件从真机设备拷贝到计算机上，在 `AIoT-IDE` 中通过 位置 3 加载进行分析。
 在 `AIoT-IDE` 中，JavaScript 堆分析和导出的工具位于功能面板区域选择 **调试 -> Snapshot -> Profile**，如下图标签1，2，3所示：
 ![alt text](../../../images/tools/ide-debug-9.png)


---
## 文件: tools\debug\multi-screens.html

# # 多屏适配

# [#](multi-screens.html#多屏适配) 多屏适配
 为了提升开发者开发体验，`AIoT-IDE`内置了****功能，可将一套代码跑在多个不同的设备屏幕上，以测试在不同屏幕上的适配效果。
 ## [#](multi-screens.html#多设备模拟器创建) 多设备模拟器创建
 为了还原最真实的多屏适配开发场景，`AIoT-IDE`提供了多个不同设备尺寸配置的模拟器和不同的镜像版本，以便在多种设备，不同镜像系统上测试应用。
 `目前提供三种镜像可供选择`
 
- `vela-miwear-watch-5.0` vela5.0稳定发布版本

 - `vela-miwear-watch-4.0` vela4.0稳定发布版本

 - `vela-watch-4.0` vela开发版本

 **目前提供六种不同屏幕尺寸的设备可供创建：**
 ![alt text](../../../images/tools/s3-watch.png) xiaomiWatch  466*466

 ![alt text](../../../images/tools/s4-watch.png) redmiWatch

 ![alt text](../../../images/tools/8-band.png) xiaomiBand

 ![alt text](../../../images/tools/8pro-band.png) xiaomiBandpro

 ![alt text](../../../images/tools/10-band.png) xiaomi10Band

 ![alt text](../../../images/tools/sound-mini.png) xiaomiSoundMini

 目前创建模拟器时，`vela-miwear-watch-5.0`和`vela-miwear-watch-4.0`只支持xiaomiWatch(466*466)尺寸一种设备，`vela-watch-4.0`支持上面全部设备类型，并且可自定义设备类型进行创建。
 注意
 模拟器与真实设备之间是有性能差异的

 ## [#](multi-screens.html#自定义模拟器) 自定义模拟器
 此外，为了支持更多尺寸的设备，创建模拟器时还可以选择**custom**自定义模拟器时，提供以下自定义配置：
 
- 1.可自定义模拟器宽高(宽高小于800大于100)，模拟器形状，模拟器密度等操作。
 - 2.可自定义并设置模拟器类型，目前主要为**watch**，**band**，**smartspeaker**
 - 3.可自定义模拟器形状，目前主要有两种形状**圆形**和**矩形**，矩形可自定义圆角
 - 4.可自定义模拟器屏幕dpi

 ![alt text](../../../images/tools/ide-emulator-4.png)
 点击底部新建按钮，完成模拟器创建后，`AIoT-IDE`右下角会弹出创建成功提示，设备管理页面会实时更新创建的模拟器。
 ![alt text](../../../images/tools/ide-emulator-5.png)
 ## [#](multi-screens.html#多屏预览) 多屏预览
 在创建完不同设备类型的模拟器后，通过`调试`或`运行`按钮启动模拟器时，**选择对应不同设备的模拟器**点击确定，即可同时运行。
 ![alt text](../../../images/tools/ide-debug-13.png)
 运行成功后，模拟器预览页将出现不同设备屏幕的预览效果
 ![alt text](../../../images/tools/ide-debug-12.gif)
 ## [#](multi-screens.html#自定义模拟器皮肤) 自定义模拟器皮肤
 此外，如果用户不仅仅满足于自定义模拟器的显示效果，希望能更逼真的预览模拟器设备，我们还提供了自定义皮肤的功能。
 ## [#](multi-screens.html#vela-模拟器皮肤组) Vela 模拟器皮肤组
 制作一款皮肤共需要两张图片和一个配置文件：
 
- `background.png` 是设备主体图，要求屏幕区域为黑色
 - `foreground.png` 用于遮挡模拟器画面以外的部分。
 - **layout配置文件**，主要是用来配置皮肤所需的`background.png`，`foreground.png`布局信息

 ![alt text](../../../images/tools/ide-skin-1.png)
 其中`foreground.png` 一般由背景图片中扣出模拟器画面部分并将屏幕部分修改为透明色。没有 `foreground.png` 与有 `foreground.png` 的对比如下:
 ![alt text](../../../images/tools/ide-skin-2.png) 未配置foreground.png

 ![alt text](../../../images/tools/ide-skin-3.png) 配置了foreground.png

 ## [#](multi-screens.html#layout-文件的制作) Layout 文件的制作
 拷贝以下代码并按照注释修改信息即可，其余部分保持不变
 ```
  parts {
    device {
      display {
        # 模拟器尺寸 
        width 466
        # 模拟器尺寸
        height 466
        # x和y填 0
        x 0
        y 0
      }
    }
    portrait {
      background {
        # 背景图片的名称
        image background.png
      }
      foreground {
        # 前景图片的名称
        mask foreground.png
      }
    }
  }
  layouts {
    portrait {
      // 整个皮肤的大小，一般使用背景图片的像素尺寸
      width 572
      height 938
      event EV_SW:0:1 
      part1 {
        name portrait
        x 0
        y 0
      }
      part2 {
        name device
        # 前景图片从背景图片中扣图时的起始坐标，以左上角为0，0计算
        x 54
        y 236
      }
    }
  }

  // 将会在创建模拟器时透传给底层配置的字段
  props {
    // 屏幕形状。可选值： circle（圆形）、rect(矩形)、pill-shaped（胶囊形屏eg: 全面屏手环）
    shape circle
    // 屏幕密度，可选值： ['120'，'140'，'160'，'180'，'213'，'240'，'280'，'320'，'340'，'360'，'400'，'420'，'440'，'480'，'560'，'640']
    density 320
    // 设备类型，可选值：phone（手机）、watch（手表）、pad（平板）、car（车机）、tv（电视）、band（手环）smartspeaker（音响），默认watch
    flavor watch
  }

```
## [#](multi-screens.html#应用皮肤文件) 应用皮肤文件
 在通过通过 `AIoT-IDE` 打开 `SKD` 目录，该目录下有一个 **skins** 目录，在 **skins/user** 目录下新建一个文件夹名称为你的皮肤名称，将制作好的文件放入其中，如果 skins下没有user，则先创建 user 目录 ，再次使用 IDE 创建模拟器时即可选择应用该皮肤。
 注意
 SDK目录是一个以点开头的目录，请提前打开操作系统的隐藏文件后再进行操作。

 ![alt text](../../../images/tools/ide-skin-4.png)
 ## [#](multi-screens.html#layout详解) layout详解
 其中的 layout 文件中的内容如下：
 ![alt text](../../../images/tools/ide-skin-5.png)
 **layout**配置内容详解：
 
- **layout** 中可定义 **portrait** 和 **landscape** 两种布局方式，即竖屏和横屏其中的**width** 和 **height** 为你的**background.png** 的像素大小

 - **event** 固定值填 **EV_SW:0:0**

 - **part1** 引用上方 parts 中定义背景图片和前景图的那个，name 表示 引用 part 的名称，x，y 表示布局开始的左边，以左上角开始为0，0

 - **part2** 引用上方 parts 中定义 **dispaly** 的那个，name 表示 引用 part 的名称，x，y 表示布局开始的坐标，以左上角开始为0，0

 - **parts** 定义皮肤的组成部分，一般又两部分组成，即皮肤和模拟器画面，带 **display** 的表示模拟器画面，带 **background** 和 **foreground** 的表示皮肤。


---
## 文件: tools\debug\start.html

# # 编译预览

# [#](start.html#编译预览) 编译预览
 运行，调试，发布，打包vela项目主要靠**顶部操作栏**中的按钮来实现，同时依赖右侧**用户引导页**和**设备管理页**来保证当前开发环境具备**模拟器运行环境**，同时展示效果具体显示在右侧的**模拟器预览界面**。
 ## [#](start.html#运行项目) 运行项目
 点击`选择设备`按钮，选择要运行的模拟器，点击确定后，再点击`调试`将启动模拟器。
 ![alt text](../../../images/tools/ide-debug-5.png)
 模拟器启动运行成功后，模拟器会推送当前项目，推送成功后，可在模拟器预览页预览效果，如下动态图所示：
 ![alt text](../../../images/tools/ide-run-1.gif)
 再次点击运行，将停止向模拟器推送当前项目，**但模拟器依然保持运行**。


---
## 文件: tools\debug\toolbar.html

# # 功能按钮

# [#](toolbar.html#功能按钮) 功能按钮
 在模拟器启动成功，进入模拟器运行页面后。`AIoT-IDE`提供了一些功能按钮方便开发者开发调试应用。
 ## [#](toolbar.html#关闭所有模拟器) 关闭所有模拟器
 **关闭所有模拟器**，位于模拟器运行页面右上角，点击后可关闭所有正在运行的模拟器，如下图中序号1所示。
 ![alt text](../../../images/tools/ide-debug-26.png)
 ## [#](toolbar.html#自定义模拟器显示) 自定义模拟器显示
 **自定义模拟器显示**，位于模拟器运行页面右上角，点击后可在弹出框中自定义已运行的模拟器的显示顺序，如下图序号1，序号2所示。
 ![alt text](../../../images/tools/ide-debug-27.png)
 ## [#](toolbar.html#关机) 关机
 关机，每个模拟器显示区域都会在右侧配置一个关机按钮，可关闭单个运行的模拟器，如下图序号1所示。
 ![alt text](../../../images/tools/ide-debug-28.png)
 ## [#](toolbar.html#首页) 首页
 首页，每个模拟器显示区域都会在右侧配置一个首页按钮，点击后可回到当前模拟器首页，如下图序号1，2所示。
 ![alt text](../../../images/tools/ide-debug-29.png)
 ## [#](toolbar.html#终端) 终端
 终端，每个模拟器显示区域都会在右侧配置一个终端按钮，点击进入模拟器命令行终端，如下图序号1，2所示。
 ![alt text](../../../images/tools/ide-debug-35.png)
 ## [#](toolbar.html#查看日志) 查看日志
 查看日志，每个模拟器显示区域都会在右侧配置一个查看日志按钮，点击进入可查看日志，如下图序号1，2所示。
 ![alt text](../../../images/tools/ide-debug-36.png)
 ## [#](toolbar.html#菜单) 菜单
 菜单，每个模拟器显示区域都会在右侧配置一个菜单按钮，点击后可回到当前模拟器菜单页，如下图序号1所示。
 ![alt text](../../../images/tools/ide-debug-30.png)
 ## [#](toolbar.html#截图) 截图
 截图，每个模拟器显示区域都会在右侧配置一个截图按钮，点击可对当前模拟器效果进行截图，并选择有皮肤，无皮肤两种模式，如下图序号1，2所示。
 ![alt text](../../../images/tools/ide-debug-31.png)
 ## [#](toolbar.html#尺寸) 尺寸
 尺寸，每个模拟器显示区域都会在右侧配置一个尺寸按钮，可动态调整当前模拟器显示大小，如下图序号1，2所示。
 ![alt text](../../../images/tools/ide-debug-32.png)
 ## [#](toolbar.html#安装rpk) 安装rpk
 安装rpk，每个模拟器显示区域都会在右侧配置一个安装rpk按钮，点击后选择本地rpk文件进行安装，如下图序号1，2所示。
 ![alt text](../../../images/tools/ide-debug-33.png)
 ## [#](toolbar.html#运行rpk) 运行rpk
 运行rpk，每个模拟器显示区域都会在右侧配置一个运行rpk按钮，点击后在已安装的rpk列表中，切换运行的rpk或卸载已安装的rpk。如下图序号1，2，3所示。
 ![alt text](../../../images/tools/ide-debug-34.png)


---
## 文件: tools\debug\watch-log.html

# # 日志查看

# [#](watch-log.html#日志查看) 日志查看
 借助各项日志（如编译日志和模拟器日志），您可以迅速了解应用当前状态，从而提高开发效率。要查看日志，只需在`功能面板`中点击`输出`选项即可。日志输出面板提供了一系列实用功能，包括：`切换日志类型`、`清空日志`、`开启/关闭自动滚动`、`导出日志` 和 `筛选日志`等。
 
- **切换日志类型**：选择需要查看的日志类型，如模拟器日志、编译日志等。
 - **清空日志**：一键清除当前类型的所有日志。
 - **开启/关闭自动滚动**：当启用时，新日志添加到输出面板时，视图将自动滚动到最新的日志条目；关闭该功能后，自动滚动将停止，日志停留在上次查看的位置。
 - **导出日志**：将当前类型的日志导出，保存至当前项目的 logs 目录下。
 - **筛选日志**：在「输出」面板聚焦状态下，使用 Command+  F（在Windows上使用 Ctrl+ F）即可触发日志关键词搜索，支持正则表达式。

 ![alt text](../../../images/tools/ide-debug-8.png)
 温馨提示：如果在开发中遇到异常，如预览黑屏或渲染内容与代码不一致，请首先查看**编译日志**以确认应用是否构建成功，然后检查 **模拟器日志**以判断应用是否正常运行。如果两者均无问题，请参阅文档或向`AIoT-IDE`官方寻求技术支持。


---
## 文件: tools\dev\build.html

# # 应用热更新

# [#](build.html#应用热更新) 应用热更新
 在使用`AIoT-IDE`开发**Xiaomi Vela JS 应用**时，还内置了热更新功能。开发者在通过点击**顶部操作栏**`运行`或`调试`按钮，运行模拟器时会时刻监听文件变化。
 ![alt text](../../../images/tools/ide-debugrun-1.png)
 在监听到文件变化会**自动触发编译**，推送当前项目，实时预览代码编译后的最新效果，当再次点击`运行`或`调试`按钮，取消推送时，将停止监听文件文件，减小性能消耗。
 ![alt text](../../../images/tools/ide-watch-1.gif)


---
## 文件: tools\dev\format.html

# # 代码美化

# [#](format.html#代码美化) 代码美化
 ## [#](format.html#自动格式化) 自动格式化
 代码自动格式化在软件开发中至关重要，它带来的好处包括增强代码一致性、提高可读性、简化审查过程，并支持敏捷开发实践。**`AIoT-IDE`** 提供此功能，但默认情况下是关闭的。要启用它，只需进行简单设置：
 使用快捷键 Command ，（Windows 系统为 Ctrl ，）打开 设置 面板。
输入format进行搜索。
 勾选「Editor: Format On Save」，如下图所示。
 ![alt text](../../../images/tools/ide-ux-5.png)
 激活此选项后，每次保存时，系统都会自动使用 prettier 格式化代码。
 **`Prettier`** 是一款流行的代码格式化工具，支持多种开发语言和文件格式，其目标是提供统一的代码风格，以简化代码维护并提高团队效率。**`AIoT-IDE`** 内置 prettier 依赖，您无需额外操作即可使用。具体效果如下动态图所示：
 ![alt text](../../../images/tools/ide-ux-12.gif)


---
## 文件: tools\dev\manifest.html

# # 可视化编辑

# [#](manifest.html#可视化编辑) 可视化编辑
 `manifest.json`作为**Xiaomi Vela JS 应用**的主要配置文件,`AIoT-IDE`中配置了可视化编辑界面，如下图所示：
 ![alt text](../../../images/tools/ide-manifest-1.png)
 点击打开manifest可视化界面后，可更直观清晰的对`manifest.json`进行编辑
 ![alt text](../../../images/tools/ide-manifest-2.png)


---
## 文件: tools\dev\start.html

# # 代码补全

# [#](start.html#代码补全) 代码补全
 `AIoT-IDE` 是基于 Visual Studio Code 二次开发的，因此它继承了 VS Code 的所有代码编辑功能。此外，为了更贴合**Xiaomi Vela JS 应用**的开发需求和提升开发者的体验，`AIoT-IDE`  内置了用于 ux 文件的语法功能，包括语法辅助和自动补全等。
 ## [#](start.html#ux-文件自动补全) ux 文件自动补全
 
- template 区域内补全

- 标签补全：输入 <（标签开头）后开始自动补全标签
 - 标签属性补全：自动补全通用属性和组件特有的属性
 - 标签属性值补全：如果存在枚举值，自动补全属性值

 ![alt text](../../../images/tools/ide-ux-6.gif)
 ## [#](start.html#style-区域内补全) style 区域内补全
 
- CSS 属性补全：支持 Css 属性的自动补全；
 - CSS 属性值补全：支持 Css 属性值的自动补全。

 ![alt text](../../../images/tools/ide-ux-7.gif)
 ## [#](start.html#其他补全) 其他补全
 
- **路径补全**：自动补全文件路径，如组件路径；
 - **class 值补全**：根据 style 标签中定义的 class 值进行自动补全。

 ![alt text](../../../images/tools/ide-ux-8.gif)
 **ux 文件定义跳转**
 
- **跳转支持**：template 区域的 class 和 id，变量，方法，自定义组件跳转，script 区域的接口跳转等；
 - **跳转操作**：Mac 下使用 command + 左键，Windows 下使用 ctrl + 左键，即可跳转至定义处。
![alt text](../../../images/tools/ide-ux-9.gif)

 **ux文件链接跳转**
 
- **链接跳转**：可以从 ux 文件跳转到链接对应的文件；
 - **跳转操作**：Mac 下使用 command + 左键，Windows 下使用 ctrl + 左键，即可实现跳转。

 ![alt text](../../../images/tools/ide-ux-10.gif)
 **ux 文件悬浮提示**
 
- **标签提示**：显示标签相关信息；
 - **属性值提示**：显示标签属性值相关信息；
 - **样式提示**：显示关于 style 的样式信息；
 - **脚本提示**：显示 script 区域的方法，导入模块等提示信息。

 ![alt text](../../../images/tools/ide-ux-11.gif)


---
## 文件: tools\devicedebug\start.html

# # 功能介绍

# [#](start.html#功能介绍) 功能介绍
 支持开发**Xiaomi Vela JS**应用时进行真机调试，在`AIoT-IDE`可通过设备ID连接真机，将应用推送到真机进行调试
 ## [#](start.html#设备升级) 设备升级
 目前真机调试只支持 O62 设备，请先联系小米工作人员获取`小米Vela设备真机调试全流程指南` 文档,按照文档内容，获取对应ota包将设备升级到支持真机调试的指定版本。
 ## [#](start.html#环境准备) 环境准备
 
1. 请使用测试版小米运动健康，连接测试用机
 1. 请保证电脑网络和手机网络在一个局域网下

 ## [#](start.html#连接) 连接
 1.打开rpk
先在真机上打开安装好的真机调试debug-app（图中红框标记的）
 ![alt text](../../../images/tools/ide-emulator-22.png)
 2.进行连接
打开debug-app后，点击开启调试，debug-app的中间按钮状态进入到【等待IDE连接】
![alt text](../../../images/tools/ide-emulator-23.png)
 3.IDE端连接
在电脑端打开AIOT-IDE，进入真机调试界面
![alt text](../../../images/tools/ide-emulator-24.png)
 4.开始连接
点击连接设备，输入设备IDE（设备ID从上面的debug-App中的设备ID获取），点击连接
 ![alt text](../../../images/tools/ide-emulator-25.png)
 5.连接成功
连接成功后，按钮下面列表中出现一条真机信息，状态显示为Connected
 ![alt text](../../../images/tools/ide-emulator-26.png)
 ## [#](start.html#调试) 调试
 在真机连接成功后，可进入调试阶段，将当前开发的vela应用在真机上调试。
 1.选择设备
在顶部tab栏中点击连接设备，选择真机设备，然后点击调试，进入真机调试模式。
 ![alt text](../../../images/tools/ide-emulator-27.png)
 ![alt text](../../../images/tools/ide-emulator-28.png)
 ![alt text](../../../images/tools/ide-emulator-29.png)
 2.进入真机调试
调试运行成功后，真机上会自动打开当前应用，AIOT-IDE底部会直接打开调试面板
 ![alt text](../../../images/tools/ide-emulator-30.png)
 3 获取日志
在真机调试面板中点击获取日志，可直接拉取真机上的日志
 ![alt text](../../../images/tools/ide-emulator-31.png)
 4.推送其他rpk
在真机调试页面中可点击推送rpk按钮，选择要推送的非当前项目rpk进行推送，进行真机调试。
 ![alt text](../../../images/tools/ide-emulator-32.png)
 5.真机调试效果
在AIOT-IDE调试面板上进行调试，真机会实时显示调试效果(但不支持热更新，如要修改源码可点击打包按钮打包当前应用，通过第四步进行rpk安装)
 ![alt text](../../../images/tools/ide-emulator-33.png) ![alt text](../../../images/tools/ide-emulator-34.png)


---
## 文件: tools\emulator\create-emulator.html

# # 设备管理

# [#](create-emulator.html#设备管理) 设备管理
 设备管理页主要分为两部分：
 
- **1.模拟器版本管理**：提供对模拟器的增删改查和运行功能，如下图1所示。
 - **2.Vela镜像版本管理**：提供对模拟器运行环境所需的SDK包的安装和更新，如下图2所示。

 ![alt text](../../../images/tools/ide-emulator-1.png)
 ## [#](create-emulator.html#模拟器设备管理) 模拟器设备管理
 模拟器设备管理，主要展示模拟器的基本信息：
 
- 名称
 - 镜像版本
 - 屏幕尺寸
 - 构建时间
 - 操作栏（提供删除，运行等功能）

 此外，用户可以点击左上角新建按钮进入模拟器创建页面。
 ![alt text](../../../images/tools/ide-emulator-20.png)
 ### [#](create-emulator.html#自动创建模拟器) 自动创建模拟器
 在初次使用 `AIoT-IDE`开发工具时，在初始化模拟器环境时，会检测本地是否已经创建过模拟器，若本地没有创建过模拟器将会提示开发者是否要自动创建模拟器。
 ![alt text](../../../images/tools/ide-emulator-13.png)
 选择确定，将会在初始化环境后自动创建一个正式版模拟器。
 ![alt text](../../../images/tools/ide-emulator-15.png)
 选择创建所有设备模拟器，将会自动创建所有设备类型的模拟器。
 ![alt text](../../../images/tools/ide-emulator-14.png)
 ### [#](create-emulator.html#新建模拟器) 新建模拟器
 点击上图**新建**按钮打开模拟器创建页面。首次进入**创建模拟器页面**，镜像版本默认为`vela-miwear-watch-5.0`，输入模拟器名称后，点击**新建**即可完成创建。如下图**标签1，2，3，4**所示：
 ![alt text](../../../images/tools/ide-emulator-3.png)
 除了默认的`vela-miwear-watch-5.0`镜像，还可以在上图**标签3**中，下拉选择`vela-miwear-watch-4.0`镜像和`vela-watch-4.0`镜像，如下图**标签1，2**所示：
 ![alt text](../../../images/tools/ide-emulator-10.png)
 在设备管理列表中，可对已创建的模拟器进行操作，目前提供了运行，暂停，删除等操作按钮。
 
- 此外，在设备管理列表中，为了方便用户操作，还提供了多个快捷配置：
 
- **复制启动命令**：复制当前模拟器的启动命令，方便用户通过命令行自启模拟器和模拟器启动失败时进行排查。

 - **打开模拟器目录**：打开模拟器文件存放目录，方便直接查看模拟器配置文件。

 - **打开镜像文件**：打开模拟器SDK包存放目录，方便直接查看模拟器SDK包文件。

 - **安装Rpk**：在模拟器运行成功后，用于可选择本地环境里Rpk包，直接安装预览（模拟器非运行状态时禁用）。

 - **自定义镜像目录**：当用于本地多有个镜像文件时，用户可以自定义镜像目录，运行自己本地的镜像文件。

 - **重置镜像目录为默认**：用户自定义镜像目录后，可以通过重置镜像目录为将镜像运行目录重置为系统默认目录。

 ![alt text](../../../images/tools/ide-emulator-9.png)
 ## [#](create-emulator.html#模拟器sdk管理) 模拟器SDK管理
 模拟器SDK管理主要分为两部分：
 
- 模拟器内核，如下图1所示
 - 模拟器镜像，如下图2所示

 ![alt text](../../../images/tools/ide-emulator-16.png)
 模拟器SDK管理页面中主要从四个维度展示模拟器信息
 
- 名称
 - 版本
 - 构建时间
 - 状态

 每次启动`AIoT-IDE`时，会自动检测模拟器内核和模拟器镜像版本是否需要更新或安装，用户可以在列表的状态一栏中，查看到模拟器内核或被模拟器镜像是否安装或是否需要升级，可手动点击安装或升级，如下图1所示
 ![alt text](../../../images/tools/ide-emulator-17.png)
 ## [#](create-emulator.html#设备使用配置) 设备使用配置
 除了上面的基础功能，模拟器设备管理页面提供可一项单的的设备使用配置（如下图1所示）：
 
- Adb Mode: 是否优先使用插件内置的adb模块，还是使用主机本地的adb模块（如下图2所示）
 - Hide Qt Window: 是否使用GRPC技术将模拟器嵌入到IDE中显示，否则在IDE外部弹出显示（如下图3所示）。

 ![alt text](../../../images/tools/ide-emulator-18.png)


---
## 文件: tools\emulator\emulator-run.html

# # 运行模拟器

# [#](emulator-run.html#运行模拟器) 运行模拟器
 在设备管理页面的模拟器列表中，点击`运行`图标，会自动运行模拟器，并且按钮进入**loading**状态。不过通过设备管理页运行模拟器成功后，并不会推送当前打开的**Xiaomi Vela JS应用项目**，还是需要点击顶部****顶部操作栏****的 `选择设备`按钮选择已运行的模拟器再点击`调试`按钮，运行当前项目。
 ![alt text](../../../images/tools/ide-emulator-11.png)
 
- 通过设备管理页运行成功后，不同的镜像将有不同的表现：
 
- `vela-miwear-watch-5.0`镜像版本的模拟器将显示表盘页面。

 - `vela-miwear-watch-4.0`镜像版本的模拟器将显示表盘页面。

 - `vela-watch-4.0`的镜像版本的模拟器，由于将显示为黑屏。

 如下图**标签1，2**所示：
 ![alt text](../../../images/tools/ide-emulator-12.png)


---
## 文件: tools\project\creat-project.html

# # 新建项目

# [#](creat-project.html#新建项目) 新建项目
 `AIoT-IDE`提供了对**Xiaomi Vela JS**应用项目的新建和管理功能。
 开发者初次使用`AIoT-IDE`时可按如下步骤打开新建项目弹窗创建项目：
 
- 1.点击左上角文件按钮，出现下拉菜单。
 - 2.点击新建项目菜单，打开新建项目弹窗。

 ![alt text](../../../images/tools/ide-create-project.png)
 在新建项目时可选择项目类型，并通过我们提供的模板创建基础的**Xiaomi Vela JS**应用项目，目前提供了八种基础模板：
 
- 1.**Xiaomi Vela JS**应用项目基础模板
 - 2.**Xiaomi Vela JS**应用项目日历模板
 - 3.**Xiaomi Vela JS**应用项目图表模板
 - 4.**Xiaomi Vela JS**应用项目列表模板
 - 5.**Xiaomi Vela JS**应用项目音乐播放器模板
 - 6.**Xiaomi Vela JS**应用项目开发示例模板
 - 7.**Xiaomi Vela JS**应用项目计算器模板
 - 8.**Xiaomi Vela JS**应用项目设置面板模板

 ![](../../../images/tools/ide-project-template.png) ![](../../../images/tools/ide-project-template1.png)
 选择完模板后，点击下一步，请按下面步骤完成创建：
 
- 1.输入项目名称
 - 2.选择创建目录(`AIoT-IDE`会记录用户曾选择过的创建目录，可直接选择)
 - 3.输入完相应表单内容，点击创建，即可创建成功。

 ![alt text](../../../images/tools/ide-create-project1.png)
 创建成功后，`AIoT-IDE`会自动打开创建的项目，按引导页指示安装项目依赖后，即可运行项目(**下图标签1，2**)。
 ![alt text](../../../images/tools/ide-create-success.png)


---
## 文件: tools\project\project.html

# # 管理项目

# [#](project.html#管理项目) 管理项目
 对通过`AIoT-IDE`新建项目创建的**Xiaomi Vela JS**应用项目，`AIoT-IDE`提供了删除和打开历史项目的功能。
 ## [#](project.html#打开历史项目) 打开历史项目
 在打开新建项目弹窗时，可查看到已创建项目的历史信息卡片，选中其中一个项目，通过右下角打开按钮打开项目，如下图1，2所示：
 ![alt text](../../../images/tools/ide-delete-project.png)
 ## [#](project.html#删除项目) 删除项目
 将鼠标移动至历史项目信息顶部的**管理**按钮，点击后每个卡片的右上角会出现一个勾选按钮，选择一个或多个历史项目后，可点击右下角删除项目。如下图**标签1，2，3**所示：
 ![alt text](../../../images/tools/ide-delete-project-1.png)
 ## [#](project.html#导入项目) 导入项目
 将鼠标移动至历史项目信息顶部的**导入**按钮，点击后将会自动打开一个文件夹选择框，选择对应的项目类型进行导入，非qucikApp项目和**Xiaomi Vela JS**应用项目将无法导入
 ![alt text](../../../images/tools/ide-delete-project-2.png)


---
## 文件: tools\project\template.html

# # 项目类型

# [#](template.html#项目类型) 项目类型
 `AIoT-IDE`不仅支持**Xiaomi Vela JS**应用项目，还支持对快应用项目的创建.
 ![alt text](../../../images/tools/ide-project.png)
 如上图**标签1，2**所示，在打开**新建项目弹窗**后，可左侧菜单栏选择**quickApp**，再点击**创建**进入创建页面，按输入提示输入创建信息，即可创建快应用项目。
 ![alt text](../../../images/tools/ide-project-1.png)


---
## 文件: tools\release\release.html

# # 发布应用

# [#](release.html#发布应用) 发布应用
 不同于顶部按钮区域的打包按钮，点击`发布`按钮发布应用，将生成**release**包。
 **release**包是为了发布到生产环境而设计的，因此它会进行更严格的优化，以减少文件大小和加载时间。通常情况下，release 只包含必要的文件和代码，会删除所有的调试信息、注释和未使用的代码，以减小文件大小并提高性能。
 同时，在生成release包前，会检查当前目录下是否**包含签名文件**，如果没有会进入创建签名页面，按提示点击完成即可创建签名文件。
 ![alt text](../../../images/tools/ide-debug-11.gif)
 签名文件**创建成功**后，再次**点击发布**即可创建release包。
 ![alt text](../../../images/tools/ide-debug-12.png)


---
## 文件: tools\release\start.html

# # 打包应用

# [#](start.html#打包应用) 打包应用
 **`Xiaomi Vela JS 应用`** 应用的封装采用了专门的 .rpk 文件格式，而在 `AIoT-IDE` 中，我们在**顶部操作栏**提供了打包，发布两个按钮对项目进行打包，如下图**标签1，2**所示。
 ![alt text](../../../images/tools/ide-debug-10.png)
 直接点击打包应用，会在功能面板执行打包名，打包成功后会在dist目录下生成debug包
 ![alt text](../../../images/tools/ide-debug-11.png)
 debug包是为了方便开发者进行调试而设计的，因此它不会进行过多的优化。通常情况下，debug 包会包含调试信息，以便开发者进行调试和定位错误。


---
## 文件: tools\start\project.html

# # 了解界面

# [#](project.html#了解界面) 了解界面
 ## [#](project.html#界面布局) 界面布局
 `AIoT-IDE` 的主窗口由几个主要区域构成，具体如下：
 1.**侧边栏**：提供资源管理器、搜索、Git 管理、插件市场、快捷入口等功能。

2.**菜单栏**：包含文件、编辑、选择、视图、转到、终端、窗口、帮助等菜单项。

3.**工具栏**：包含修运行、调试、重启、打包、发布等功能按钮选项。

4.**代码编辑区**：包含代码编辑、定义跳转、代码补全、错误提示等功能，详情参阅代码补全。

5.**功能面板**：提供问题、输出、终端、调试等面板。

6.**模拟器**：包含引导页提示，仿真预览、模拟真机操作、截图等功能。
 ![alt text](../../../images/tools/ide-tools.png)
 只有通过`AIoT-IDE` 打开的是一个**Xiaomi Vela JS应用**，主窗口界面才会如上图所示，`AIoT-IDE`会通过打开的项目结构自动识别打开的项目是否为**Xiaomi Vela JS应用项目**。
 ## [#](project.html#工具栏界面) 工具栏界面
 `AIoT-IDE` 工具栏界面中含有几个常用功能：
 **选择设备**：选择本地创建的模拟器
 **调试**：编译预览当前打开的**Xiaomi Vela JS**应用项目，并打开调试面板，调试当前项目
 **设备**：打开设备管理页面，创建不同镜像类型，设备类型的模拟器
 **打包**：将当前**Xiaomi Vela JS**应用项目打包为rpk
 **发布**：生成 release类型的应用包（RPK）
 另外，`AIoT-IDE`支持直接预览`Xiaomi Vela JS`应用项目打包后的rpk，可将rpk解压后的目录通过`AIoT-IDE`打开，可对rpk进行预览。
 ## [#](project.html#模拟器界面) 模拟器界面
 模拟器界面主要包含三部分:
 
- **用户引导页**
 - **模拟器SDK和设备管理更新页**
 - **模拟器运行预览页面**

 ## [#](project.html#用户引导页) 用户引导页
 模拟器**用户引导页**会引导用户初始化**Xiaomi Vela JS**应用模拟器运行环境。按引导页提示进行操作:
 
- 1.**安装项目依赖**，等待项目依赖和环境安装完成，才能正常编译预览**Xiaomi Vela JS**应用项目
 - 2.**初始化模拟器环境**，模拟器用户引导页会自动当前环境是否具备模拟器运行环境，如不具备可按用户引导页操作，**自动安装**好模拟器环境

 ![alt text](../../../images/tools/ide-warning.png)
 按照上图引导页提示**正确操作**完毕后，引导页会给出项目可以**当前项目可以正常启动**的提示，如下图中**标签1**所示。
 ![alt text](../../../images/tools/ide-success.png)
 注意：**出于性能考虑**，引导页不会轮询监测项目依赖和模拟器运行环境是否已经具备，当用户选择**自行手动安装**的方式，安装好项目依赖和模拟器运行环境时，可点击**引导页右上角刷新**按钮，刷新引导页状态。
 ![alt text](../../../images/tools/ide-sx.png)
 ## [#](project.html#设备管理页) 设备管理页
 设备管理页主要分为两部分：
 
- **1.模拟器管理和真机调试**：提供对模拟器的增删改查和运行功能和真机调试
 - **2.模拟器SDK管理**：提供对模拟器运行环境所需的SDK包的安装和更新

 ![alt text](../../../images/tools/ide-emulator-1.png)
 ![alt text](../../../images/tools/ide-emulator-19.png)
 ## [#](project.html#模拟器运行预览页面) 模拟器运行预览页面
 模拟器预览页是通过grpc技术将运行的模拟器内嵌到`AIoT-IDE`中，进行预览显示。当项目依赖和模拟器环境具备后，可按如下步骤预览当前项目：
 
- 1.点击**顶部操作栏**的**选择设备**按钮，选择一个或多个要运行的**模拟器**。
 - 2.点击****顶部操作栏****的**调试**按钮，运行模拟器，按钮进入**loading状态**，运行成功后，会变为蓝色。
 - 3.底部工具栏开始输出模拟器运行日志，页面从用户引导页自动切换模拟器预览页面。
 - 4.模拟器运行成功，模拟器预览页将出现对应模拟器，并将当前打开的**Xiaomi Vela JS应用**推送到运行的模拟器中。

 ![alt text](../../../images/tools/ide-debugrun.png)


---
## 文件: tools\toolkit\start.html

# # AIoT-toolkit

# [#](start.html#aiot-toolkit) AIoT-toolkit
 开发者在开发`Xiaomi Vela JS 应用`项目时，`AIoT-IDE`主要通过项目模板中内置的`AIoT-toolkit`完成项目的编译构建任务，得到构建文件（以 rpk 后缀命名，如：com.application.demo.rpk）。
 ## [#](start.html#功能支持) 功能支持
 `AIoT-toolkit`是将**源码项目**转换为**目标代码项目**并生成**目标代码应用**的`命令行工具`，同时配备了 模拟器 相关功能供开发者使用。`AIoT-toolkit`提供了脱离`AIoT-IDE`，独立开发`Xiaomi Vela JS` 应用的能力。在不使用`AIoT-IDE`的情况下，可以完全通过`AIoT-toolkit`使用命令行工具进行开发，目前最新的**AIoT-toolkit2.0**有以下常用命令：
 
- 创建项目：**npm create aiot**
 - 直接运行：**aiot start**，第一次运行会提示创建的模拟器，根据提示操作即可
 - 构建项目，生成rpk： **aiot build**
 - 构建项目-release模式：**aiot release**
 - 获取已连接设备列表: **aiot getConnectedDevices**
 - 获取设置平台：**aiot getPlatforms**
 - 创建`Xiaomi Vela JS`模拟器：**aiot crateVelaAvd**
 - 删除`Xiaomi Vela JS`模拟器：**aiot deleteVelafangAvd**

 ## [#](start.html#版本支持) 版本支持
 目前`AIoT-IDE`支持**AIoT-toolkit1.0**，和**AIoT-toolkit2.0**，对**AIoT-toolkit1.0**的支持最小版本为`1.0.18`，对**AIoT-toolkit2.0**的支持最小版本为`2.0.x`。当前最新的`AIoT-IDE`**1.6.0**版本，推荐使用**2.0**，**2.0**提供了更快的编译速度和热更新支持，将给开发者提供更好的开发体验。
 `AIoT-IDE`在打开一个`Xiaomi Vela JS`应用时，会主动检测**AIoT-toolkit**版本，当前项目使用的是**AIoT-toolkit1.0时**会提示你可以升级并查看迁移文档，如下图**标签1**所示：
 ![alt text](../../../images/tools/ide-toolkit-1.png)
 如果你项目中使用了**AIoT-toolkit2.0**，但不是当前`AIoT-IDE`支持的最小正式版本，则会强制提示你升级。如下图**标签1**所示：
 ![alt text](../../../images/tools/ide-toolkit-2.png)
 ## [#](start.html#功能优化) 功能优化
 对比**AIoT-toolkit1.0**，**AIoT-toolkit2.0**有以下重大改进：

1.模板语法中可以直接写复杂函数
 ```
<div 
    id="{{(x=> x+ y)(1)}}" 
    onclick="(evt)=>{
       const x = 10;
       return sum(x, evt, y)
    }">

```
2.class的变量可以包含多个类名(之前每个变量只能包含1个类名)
 ```
class="a {{x}}"  // x="a1 a2 a3"

```
3.style可以是string，也可以是object(之前只是object)
 ```

```
4.样式顺序可以随意写(之前必须按固定顺序)
 ```
 border: solid red 10px; 

```
5.错误提示定位到行列
![alt text](../../../images/tools/ide-toolkit-3.png)


---
## 文件: tools\toolkit\update.html

# # 升级迁移

# [#](update.html#升级迁移) 升级迁移
 相较于**AIoT-toolkit1.0**，**AIoT-toolkit2.0**对`Xiaomi Vela JS`项目编译的速度有了极大的提升，同时也带来一些**破坏性更新**。在老项目使用了**1.0**现准备向2.0**升级迁移**时，请按下面的**注意事项**，对源代码进行轻微修改。
 ## [#](update.html#注意事项) 注意事项
 开发者在从**AIoT-toolkit1.0**升级到**AIoT-toolkit2.0**有以下注意事项:
 | 描述  | 解决办法  | 有些语法修正 
1. `{{}}` 中无需再嵌套`{{}}`，`onclick="{{fun({{x}}，{{y}})}}`" 改为 `onclick="fun(x, y)"` 

2.不支持的样式选择器报错，例如伪类  | 修改源代码  | 动态路径没有转换为完整的路径：
**1.0写法**：../../common  
**2.0写法**：/common/****  | 修改源代码  还有一些特殊的动态css值，从**AIoT-toolkit1.0**升级到**AIoT-toolkit2.0**，也要使用新的写法:
 
- transform

 ```
 this.divStyle = {
   transform: JSON.stringify({
       translateX: "10px",
       translateY: "20px",
       scaleX: 2,
       scaleY: 0.5,
       rotate: "10deg",
   }),
 };

```

- background
 ```
  // 线性渐变
  this.divStyle = {
      background: JSON.stringify({
          values: [
          {
              type: "linearGradient",
              directions: ["to", "left"],
              values: ["#FF0000 10px", "#0000FF 100%"],
          },
          ],
      }),
  };
  // 径向渐变
  this.divStyle = {
      background: JSON.stringify({
          values: [{
              type: "radialGradient",
              size: ["farthest-corner"],
              directions: ["center"],
              values: ["#3f87a6", "#ebf8e1", "#f69d3c"],
          }],
      }),
  };

```

 - filter

 ```
   this.divStyle = {
       filter: JSON.stringify({
           blur: "10px",
       }),
   };

```

- url

 ```
this.divStyle = {
   backgroundImage: "/common/logo.png",
};

```
注意
 以上改动请务必按照注意事项中的说明进行修改，否则在升级到**2.0**后将影响项目的正常运行和启动。

