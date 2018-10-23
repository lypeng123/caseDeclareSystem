# 大数据申报系统

## /declare_test

### 全局js或css影响所有页面，谨慎修改

### /css
#### 全局:
* common.css （固定UI部分或者插件的样式）
#### 局部:
* 除全局css外剩余的部分

### /js
#### 全局:
* common.js  （工具函数,插件,验证等公共逻辑）
* getPath.js  （前后端交互的所有api接口）
* jquery-1.11.3.js （第三方库）
#### 局部:
* 除全局js剩余的部分

### /images
* 图片资源

### /templet
* 申报表单模板，多个页面预览/查看时调用的页面，模板渲染接受3个参数 user_id,html_url,file_type

### /iframe
* 案例筛选页面下的3个子页面（包含html,css,js）
### /laydate
* 日期插件