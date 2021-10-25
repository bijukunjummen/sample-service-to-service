(this["webpackJsonpservice-to-service-ui"]=this["webpackJsonpservice-to-service-ui"]||[]).push([[0],{23:function(e,s,a){},34:function(e,s,a){"use strict";a.r(s);var c=a(1),r=a.n(c),l=a(15),t=a.n(l),n=(a(23),a(11)),o=a(2),i=a(9),d=a(7),j=a(18);function m(e){return fetch("/caller/messages",{headers:{"Content-Type":"application/json"},method:"POST",body:JSON.stringify(e)}).then((function(e){return e.json()})).then((function(e){return e}))}var b=a(0),h=function(e){var s=e.formErrors;return Object(b.jsx)("div",{children:Object.keys(s).map((function(e,a){return s[e].length>0?Object(b.jsxs)("p",{className:"alert alert-warning",children:[e," ",s[e]]},e):""}))})},u=function(e){var s=e.loading;return Object(b.jsxs)("div",{className:"row",children:[Object(b.jsx)("div",{className:"col-sm-2"}),Object(b.jsxs)("div",{className:"col-sm-8",children:[s&&Object(b.jsx)("div",{className:"progress",children:Object(b.jsx)("div",{className:"progress-bar progress-bar-striped progress-bar-animated w-100",role:"progressbar","aria-valuenow":100,"aria-valuemin":0,"aria-valuemax":100})}),Object(b.jsx)("div",{className:"col-sm-2"})]})]})},O=function(e){var s=e.responseMessage,a=e.responseError;return Object(b.jsx)("div",{className:"row mt-3",children:Object(b.jsxs)("div",{className:"col-sm-12",children:[s&&Object(b.jsxs)("div",{className:"alert alert-success",children:[Object(b.jsxs)("div",{className:"row",children:[Object(b.jsx)("label",{htmlFor:"id",className:"col-sm-2",children:"Id : "}),Object(b.jsx)("span",{className:"col-sm-4",children:s.id})]}),Object(b.jsxs)("div",{className:"row",children:[Object(b.jsx)("label",{htmlFor:"message",className:"col-sm-2",children:"Recieved : "}),Object(b.jsx)("span",{className:"col-sm-4",children:s.received})]}),Object(b.jsxs)("div",{className:"row",children:[Object(b.jsx)("label",{htmlFor:"ack",className:"col-sm-2",children:"Ack : "}),Object(b.jsx)("span",{className:"col-sm-4",children:s.ack})]}),Object(b.jsxs)("div",{className:"row",children:[Object(b.jsx)("label",{htmlFor:"statusCode",className:"col-sm-2",children:"Status Code : "}),Object(b.jsx)("span",{className:"col-sm-4",children:s.statusCode})]}),Object(b.jsxs)("div",{className:"row",children:[Object(b.jsx)("label",{htmlFor:"roundTripTimeMillis",className:"col-sm-2",children:"Round Trip Time(millis): "}),Object(b.jsx)("span",{className:"col-sm-4",children:s.roundTripTimeMillis})]})]}),a&&Object(b.jsx)("div",{className:"alert alert-warning",children:Object(b.jsx)("div",{className:"row",children:Object(b.jsx)("span",{className:"col-sm-4",children:a})})})]})})},v=function(e){e.payload;var s=Object(c.useState)({payload:"dummy payload",delay:100,responseCode:200,formErrors:{},loading:!1,formValid:!0,responseMessage:void 0,responseError:void 0}),a=Object(j.a)(s,2),r=a[0],l=a[1],t=function(e,s){var a=arguments.length>2&&void 0!==arguments[2]?arguments[2]:200;l((function(e){return Object(d.a)(Object(d.a)({},e),{},{loading:!0})})),m({payload:e,delay:s,responseCode:a}).then((function(e){l((function(s){return Object(d.a)(Object(d.a)({},s),{},{responseMessage:e,loading:!1})}))})).catch((function(e){l((function(s){return Object(d.a)(Object(d.a)({},s),{},{responseError:e.message,loading:!1})}))}))},n=function(e){var s=e.target.name,a=e.target.value;o(s,a)},o=function(e,s){var a=r.formErrors,c=!0,t=!0;switch(e){case"payload":c=s.length>=2,a.payload=c?"":" should have atleast 2 characters";break;case"delay":t=!isNaN(s)&&Number(s)>0,a.delay=t?"":" is not valid"}l((function(r){var l;return Object(d.a)(Object(d.a)({},r),{},(l={},Object(i.a)(l,e,s),Object(i.a)(l,"formErrors",a),Object(i.a)(l,"formValid",c&&t),l))}))};return Object(b.jsxs)("div",{children:[Object(b.jsxs)("div",{className:"row",children:[Object(b.jsxs)("div",{className:"col-sm-12",children:[Object(b.jsx)("h3",{children:"Send a request"}),Object(b.jsx)("p",{className:"lead alert alert-info",children:'The request will be sent to the appication which will reply after the specified "delay"'})]}),Object(b.jsx)("div",{children:Object(b.jsx)(h,{formErrors:r.formErrors})})]}),Object(b.jsx)("div",{className:"row",children:Object(b.jsx)("div",{className:"col-sm-12",children:Object(b.jsxs)("form",{onSubmit:function(e){r.loading||(l((function(e){return Object(d.a)(Object(d.a)({},e),{},{responseError:void 0,responseMessage:void 0})})),t(r.payload,r.delay,r.responseCode)),e.preventDefault()},children:[Object(b.jsxs)("div",{className:"form-group row mb-3",children:[Object(b.jsx)("label",{htmlFor:"payload",className:"col-sm-2 col-form-label",children:"Payload"}),Object(b.jsx)("div",{className:"col-sm-10",children:Object(b.jsx)("textarea",{name:"payload",className:"form-control",placeholder:"Payload",onChange:n,value:r.payload})})]}),Object(b.jsxs)("div",{className:"form-group row mb-3",children:[Object(b.jsx)("label",{htmlFor:"delay",className:"col-sm-2 col-form-label",children:"Delay (in ms)"}),Object(b.jsx)("div",{className:"col-sm-10",children:Object(b.jsx)("input",{name:"delay",type:"number",className:"form-control",placeholder:"delay",value:r.delay,onChange:n})})]}),Object(b.jsxs)("div",{className:"form-group row mb-3",children:[Object(b.jsx)("label",{htmlFor:"delay",className:"col-sm-2 col-form-label",children:"Response Status Code"}),Object(b.jsx)("div",{className:"col-sm-10",children:Object(b.jsx)("input",{name:"responseCode",type:"number",className:"form-control",placeholder:"status code",value:r.responseCode,onChange:n})})]}),Object(b.jsx)("div",{className:"form-group row mb-3",children:Object(b.jsxs)("div",{className:"col-sm-10",children:[!r.loading&&Object(b.jsx)("button",{name:"submit",className:"btn btn-primary",disabled:!r.formValid,children:"Submit"}),r.loading&&Object(b.jsx)("button",{name:"submit",className:"btn btn-primary disabled",disabled:!r.formValid,children:"Submit"})]})})]})})}),Object(b.jsx)(u,{loading:r.loading}),Object(b.jsx)(O,{responseMessage:r.responseMessage,responseError:r.responseError})]})},p=function(){return Object(b.jsx)(n.a,{children:Object(b.jsxs)("div",{children:[Object(b.jsxs)("nav",{className:"navbar navbar-expand-md navbar-dark fixed-top bg-dark",children:[Object(b.jsx)(n.b,{to:"/",className:"navbar-brand",children:"Service To Service Call Client"}),Object(b.jsx)("button",{className:"navbar-toggler",type:"button","data-toggle":"collapse","data-target":"#navbarsExampleDefault","aria-controls":"navbarsExampleDefault","aria-expanded":"false","aria-label":"Toggle navigation",children:Object(b.jsx)("span",{className:"navbar-toggler-icon"})}),Object(b.jsx)("div",{className:"collapse navbar-collapse",id:"navbarsExampleDefault",children:Object(b.jsx)("ul",{className:"navbar-nav mr-auto",children:Object(b.jsx)("li",{className:"nav-item active",children:Object(b.jsx)(n.b,{to:"/",className:"nav-link",children:"Home"})})})})]}),Object(b.jsx)("div",{className:"containerFluid",children:Object(b.jsx)("div",{className:"row",children:Object(b.jsx)("div",{className:"col-sm-10 offset-sm-1",children:Object(b.jsx)(o.a,{exact:!0,path:"/",component:v})})})})]})})},x=function(e){e&&e instanceof Function&&a.e(3).then(a.bind(null,35)).then((function(s){var a=s.getCLS,c=s.getFID,r=s.getFCP,l=s.getLCP,t=s.getTTFB;a(e),c(e),r(e),l(e),t(e)}))};a(33);t.a.render(Object(b.jsx)(r.a.StrictMode,{children:Object(b.jsx)(p,{})}),document.getElementById("root")),x()}},[[34,1,2]]]);
//# sourceMappingURL=main.64634a9e.chunk.js.map