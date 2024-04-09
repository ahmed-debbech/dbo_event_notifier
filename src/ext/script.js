/**
 * 
 */


if(window.location.href.indexOf("dboglobal.to") > -1){

    window.onload = () =>{
            setInterval(() => {
                   location.reload()
            },60000)
            //}, 1000)
          var xhr = new XMLHttpRequest();
          xhr.open('POST', 'http://localhost:7500/api/html', true);
          xhr.onload = function () {
              // do something to response
              console.log(this.responseText);
          };
          xhr.send(document.documentElement.outerHTML);
    }
}
