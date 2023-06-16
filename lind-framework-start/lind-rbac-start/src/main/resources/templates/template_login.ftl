<#macro registrationLayout bodyClass="默认值，子页面没有设置会使用这个值，子页面设置后会覆盖它" mainCss="red">
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <title><#nested "head"></title>
        <meta name="viewport" content="width=device-width,initial-scale=1.0"/>
        <link rel="stylesheet" href="static/css/iview.css">
        <link rel="stylesheet" href="static/css/site.css">
        <script charset="UTF-8" src="static/js/vue.min.js"></script>
        <script charset="UTF-8" src="static/js/axios.min.js"></script>
        <script charset="UTF-8" src="static/js/iview.min.js"></script>
        <script charset="UTF-8" src="static/lib/index.js" type="module"></script>
        <script charset="UTF-8" src="static/lib/alert.js" type="module"></script>

        <style>
            body {
                text-align: center;
            }

            .flex {
                /* 流式布局 */
                display: -webkit-flex;
                /* Safari */
                display: flex;
                flex-direction: column;
                /* 项目的排列方向 row | row-reverse | column | column-reverse; */
                flex-wrap: wrap;
                /* 换行 nowrap | wrap | wrap-reverse; */
                justify-content: center;
                /* 项目在主轴上的对齐方式 flex-start | flex-end | center | space-between | space-around; */
                align-items: center;
                /* 项目在交叉轴上如何对齐 flex-start | flex-end | center | baseline | stretch; */
                align-content: center;
                /* 多根轴线的对齐方式 flex-start | flex-end | center | space-between | space-around | stretch; */
                /* order\flex-grow\flex-shrink\flex-basis\flex\align-self */
            }
        </style>
    </head>
    <body>
    <div id="app">
        <#nested "form">
    </div>
    </body>
    </html>
</#macro>
