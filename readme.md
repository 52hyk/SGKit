## 属性
| 属性 | 描述    | 类形           | 型默认值 |
| --- |-------|--------------| ---|
| options | 展示的选项 | List<Object> | null |
| dropDownPosition | 展开方向  | enum         | Top |
| dropDownHeight | 下拉框高度 | int          | 0 |
| dropDownWidth | 下拉框宽度 | int          | 0 |
| maxHeight | 最大的高度 | int          | 0 |
| maxWidth | 最大的宽度 | int          | 0 |
| isDismissOnTouchOutside | 点击其他区域是否取消选择 | Boolean      | true |
| isDismissOnBackPressed | 按返回键是否消失     | Boolean      | true |
| animationDuration | 展开收起动画的时间    | int          | 300 |
| hasShadowBg | 是否有半透明的背景    | int          | 300 |
| useColumn | 使用多列标签样式     | int          | 3 |
| multiple | 是否支持多选       | boolean      | false |

## 内置UI方法，不适用自定义UI
| 方法名 | 描述 | 返回类形 | 型默认值 |
| --- | --- | ---| ---|
| selectedValue | 当前选择的选项标识 | void | null |
| onOptionClick | 点击选项时触发的回调函数 | void | null |
| onOptionChange | 当选项改变时触发的回调函数 | void | null |

