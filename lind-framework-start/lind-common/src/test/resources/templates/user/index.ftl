<h1>标题：${title}</h1>
<h2>列表：</h2>
<ul class="<#if users?size gt 2>${redCss!}</#if>">
    <#list users as p>
        <li>${p.name}</li>
    </#list>
</ul>