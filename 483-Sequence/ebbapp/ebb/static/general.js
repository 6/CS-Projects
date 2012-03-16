/* 
Function to shuffle an array.
Author: Jonas Raoni Soares Silva, http://jsfromhell.com/array/shuffle [v1.0]
*/
shuffle = function(o){ //v1.0
    for(var j, x, i = o.length; i; j = parseInt(Math.random() * i), x = o[--i], o[i] = o[j], o[j] = x);
    return o;
};

// create trim for old browsers
if(typeof String.prototype.trim !== 'function') {
    String.prototype.trim = function() {
        return this.replace(/^\s+|\s+$/g, ''); 
    }
}

// is an input empty
function isEmpty(idref){
    return idref.val().trim() == "";
}

// @return an integer between 0 and @param max, inclusive
function genRandom(maxInt){
    return Math.floor(Math.random()*(maxInt+1));
}

// return int between 0 and maximum not in Array arr 
function genRandomNotIn(arr, maximum){
    var rand = genRandom(maximum);
    while(jQuery.inArray(rand,arr) != -1) {
        rand = genRandom(maximum);
    }
    return rand;
}

// logging function (disable logging on production site)
function log(stuff) {
    //console.log(stuff);
}

/* SEARCHBAR
function resetBar(sref){
    if(isEmpty(sref)) sref.val("Search Games...");
}
$('#searchbar').focus(function() {
    if($(this).val() == "Search Games...") $(this).val('');
});
$('#searchbar').blur(function() {
    resetBar($(this));
});*/
  
// DOCUMENT LOADED
$(document).ready(function() {
    //resetBar($('#searchbar'));
    $("#addmorebutton").click(addNewQA);
    $("#sharelink").click(selectAllThis);
    $(".removeterm").click(removeTerm);
    $("#allimg").click(setAllImg);
    $("#alltext").click(setAllText);
    $(".newsettext").click(function(){
        setDefText(this);
    });
    $(".newsetimg").click(function(){
        setDefImg(this);
    });
});

function addNewQA(){
    $('<tr><td class="borderleft"><input name="prompt" class="qainput fancyinput fancytext" type=text value=""></td><td><a href="#text" class="newsettext active">Text</a>&nbsp;&nbsp;<a class="newsetimg" href="#image">Image</a><input name="answer" class="qainput fancyinput fancytext" type=text value=""><input name="answer_inactive" class="hidden qainput fancyinput fancyfile" type=file><input class="deftype" type="hidden" name="deftype" value="text"></td><td class="c"><a class="removeterm" href="#delete">Remove</a></td></tr>').appendTo("#data");
    //TODO better way?
    $(".removeterm").click(removeTerm); 
    $(".newsettext").click(function(){
        setDefText(this);
    });
    $(".newsetimg").click(function(){
        setDefImg(this);
    });
    return !1;
}

function removeTerm(){
    $(this).parent().parent().empty();
}
function selectAllThis(){
    $(this).select();
}

function setDefText(el){
    $(el).next().removeClass("active");
    $(el).addClass("active");
    $(el).siblings(".fancyfile").addClass("hidden");
    $(el).siblings(".fancytext").removeClass("hidden");
    $(el).siblings(".deftype").val("text");
    setDefTypeSwap(el);
}

function setDefImg(el){
    $(el).prev().removeClass("active");
    $(el).addClass("active");
    $(el).siblings(".fancytext").addClass("hidden");
    $(el).siblings(".fancyfile").removeClass("hidden");
    $(el).siblings(".deftype").val("img");
    setDefTypeSwap(el);
}

function setDefTypeSwap(clickref){
    // swap names
    $(clickref).siblings("[name='answer']").attr('name','answer_todo');
    $(clickref).siblings("[name='answer_inactive']").attr('name','answer');
    $(clickref).siblings("[name='answer_todo']").attr('name','answer_inactive');    
}

function setAllText(){
    $(".newsettext").each(function(index,element){
        setDefText(element);
    });
}

function setAllImg(){
    $(".newsetimg").each(function(index,element){
        setDefImg(element);
    });
}

