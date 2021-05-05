function getPathTo(element) {
    if (element.tagName == 'HTML')
        return '/HTML[1]';
    if (element===document.body)
        return '/HTML[1]/BODY[1]';
    var ix= 0;
    var siblings= element.parentNode.childNodes;
    for (var i= 0; i<siblings.length; i++) {
        var sibling= siblings[i];
        if (sibling===element)
            return getPathTo(element.parentNode)+'/'+element.tagName+'['+(ix+1)+']';
        if (sibling.nodeType===1 && sibling.tagName===element.tagName)
            ix++;
    }
}

var observerFunction = function(mutations, observer){
    console.log(mutations.length);

    for(i=0;i<mutations.length;i++){
        target = mutations[i].target;     

        if( observedElements.includes(mutations[i].target)) {
            continue;
           }
            
        // console.log(mutations[i].type + '' + target);
        localStorage.setItem("mutationData", localStorage.mutationData+";"+ mutations[i].type + "-" + getPathTo(target));
        observedElements.push(mutations[i].target);
    }
}; 

var observer = new MutationObserver(observerFunction);

var observedElements = [];

if(localStorage.getItem("mutationData")==null){localStorage.setItem("mutationData", "");}

observer.observe(document, {attributes:true, characterData:true, subtree:true, childList:true, subtree:true});
