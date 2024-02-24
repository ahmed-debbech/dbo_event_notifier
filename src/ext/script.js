/**
 * 
 */

window.onload = () =>{
    if(window.location.href.indexOf("dboglobal.to") > -1){
       console.log(document.documentElement.outerHTML)

      var xhr = new XMLHttpRequest();
      xhr.open('POST', 'http://localhost:7500/api/html', true);
      xhr.onload = function () {
          // do something to response
          console.log(this.responseText);
      };
      xhr.send(document.documentElement.outerHTML);
    }
}
