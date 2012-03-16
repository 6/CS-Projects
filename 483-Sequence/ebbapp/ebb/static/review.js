var curQ = 0; // index of current prompt/answer
var swap = false;
var alwaysshow = false;
 
var data;
$(document).ready(function() {
    parseData();
    changeQ();
    $("#swap").click(function() {
        // when the swap button is clicked, swap the current question and answer
        swap = !swap;
        var toBeAnswer = $("#prompt").html();
        $("#prompt").html($("#answer").html());
        $("#answer").html(toBeAnswer);
        if($("#answer").hasClass("text") && $("#prompt").hasClass("image")) {
            $("#answer").removeClass("text");
            $("#prompt").removeClass("image");
            $("#answer").addClass("image");
        }
        else if($("#answer").hasClass("image")) {
            //$("#prompt").removeClass("image");
        }
        if(swap){
            $("#swapbtn").addClass("pressed");
        }
        else{
            $("#swapbtn").removeClass("pressed");
        }
        return !1;
    });
    $("#alwaysshow").click(function() {
        alwaysshow = !alwaysshow;
        if(alwaysshow){
            $("#answer").show(200);
            $("#alwaysbtn").addClass("pressed");
            $("#answerbtn").addClass("pressed");
        }
        else{
            $("#answer").hide();
            $("#alwaysbtn").removeClass("pressed");
            $("#answerbtn").removeClass("pressed");
        }
        return !1;
    });
    $("#show").click(function() {
        if($("#answer").is(":visible")){
            // hide the answer
            $("#answer").hide();
            $("#answerbtn").removeClass("pressed");
        }
        else {
            // show the answer
            $("#answer").show(200);
            $("#answerbtn").addClass("pressed");
        }
        if(alwaysshow){
            alwaysshow = false;
            $("#answer").hide();
            $("#alwaysbtn").removeClass("pressed");
        }
        return !1;
    });
    $("#prev").click(function() {
        curQ -= 1;
        if(curQ < 0){
            // go to end
            curQ = data.length - 1;
        }
        changeQ();
        return !1; 
    });
    $("#next").click(function() {
        curQ += 1;
        if(curQ > (data.length - 1)){
            // go to beginning
            curQ = 0;
        }
        changeQ();
        return !1; 
    });
});

function parseData() {
    log("parseData");
    try {
        data = jQuery.parseJSON($('#data').val()); 
        // preload any images (TODO link)
        for(var i=0;i<data.length;i++){
            if(data[i].type == "blob"){
                $("<img src='"+data[i].prompt+"' alt='"+data[i].prompt+"'>").appendTo("#imagedumpster");
            }
        }
    } catch(e) {
        alert("Invalid JSON");
    }
}
 
function changeQ(){
    // modify the prompt and answer and hide the answer
    var newPrompt = data[curQ].prompt;
    var newAnswer = data[curQ].answer;
    var newType = data[curQ].type;
    if(swap) {
        var toBeAnswer = newPrompt;
        newPrompt = newAnswer;
        newAnswer = toBeAnswer;
    }
    $("#counter").html("<span>"+(curQ+1)+"</span>/ "+data.length);
    if(newType == "text"){
        $("#prompt").removeClass("image");
        $("#prompt").addClass("text");
        $("#answer").addClass("text");
        $("#caption").hide();
    }
    else{
        $("#prompt").removeClass("text");
        $("#prompt").addClass("image");
        var newCaption = data[curQ].caption;
        $("#caption").text(newCaption);
        if(swap){
            newAnswer = "<img class=shrink src='"+newAnswer+"'>";
            $("#answer").removeClass("text");
        }
        else {
            newPrompt = "<img class=shrink src='"+newPrompt+"'>";
            $("#answer").addClass("text");
        }
    }
    if(!alwaysshow){
        $("#answer").hide();
        $("#answerbtn").removeClass("pressed");
    }
    $("#prompt").html(newPrompt);
    $("#answer").html(newAnswer);
}
