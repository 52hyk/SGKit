## 属性
| 属性 | 描述 | 类形 | 型默认值 |
| --- |----|----|---|
| options | 展示的选项 | List<Object> | null |
| dropDownPosition | 展开方向 | enum | Top |
| dropDownHeight | 下拉框高度 | int | 0 |
| dropDownWidth | 下拉框宽度 | int | 0 |
| maxHeight | 最大的高度 | int | 0 |
| maxWidth | 最大的宽度 | int | 0 |
| isDismissOnTouchOutside | 点击其他区域是否取消选择 | Boolean | true |
| isDismissOnBackPressed | 按返回键是否消失 | Boolean | true |
| animationDuration | 展开收起动画的时间 | int | 300 |
| hasShadowBg | 是否有半透明的背景 | int | true |
| isRequestFocus | 弹窗是否强制抢占焦点 | Boolean | true |
| autoFocusEditText | 是否让输入框自动获取焦点 | Boolean | true |
| isClickThrough | 是否点击透传，默认弹背景点击是拦截的 | Boolean | true |
| isDestroyOnDismiss | 是否关闭后进行资源释放 | Boolean | true |
| notDismissWhenTouchInArea | 当触摸在这个区域时，不消失 | ArrayList<Rect> | null |
| useColumn | 使用多列标签样式 | int | 3 |
| multiple | 是否支持多选 | boolean | false |
| sgKitText | 设置Text文本，支持富文本 | CharSequence | 内容 |
| sgKitTextSize | 设置Title文本大小 | float | 16sp |
| sgKitTextColor | 设置Title文本颜色 | int | #222222 |
| sgKitTypeface | 设置Title文本Typeface(如“加粗”) | Typeface | Typeface.DEFAULT |
| sgFontIconTextSize | 设置FontIcon文本大小 | float | 16sp |
| sgFontIconTextColor | 设置FontIcon文本颜色 | int | #222222 |
| sgFontIconText | 设置FontIcon文本，支持富文本 | CharSequence | &#xe98d; |
| sgItemCheckedTextColor | item选中文本颜色 | int | #FF8100 |
| sgItemUnCheckedTextColor | item未选中文本颜色 | int | #222222 |
| sgItemDisableTextColor | item中Disable文本颜色 | int | #CCCCCC |
| sgItemCheckedTextBgColor | item选中后的背景颜色 | Drawable |  |
| sgItemUnCheckedTextBgColor | item未选中后的背景颜色 | Drawable |  |

## 内置UI方法，不适用自定义UI
| 方法名 | 描述 | 返回类形 | 型默认值 |
| --- | --- | ---| ---|
| selectedValue | 当前选择的选项标识 | void | null |
| onOptionClick | 点击选项时触发的回调函数 | void | null |
| onOptionChange | 当选项改变时触发的回调函数 | void | null |

