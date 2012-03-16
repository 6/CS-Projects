/* Based off code from:
 * http://www.nealgrosskopf.com/tech/thread.php?pid=62
 */

//Placed outside .ready for scoping
var targetX, targetY, ajaxCreatePath, ajaxDeletePath, ajaxStatsPath, isCreator, isGame;
var tagCounter = 0;
var curTagContent = '';

// for game
var tags = [];
var correctIdxs;
var curQ = null;
var controlsDisabled = true;
// default background color of answers
var defaultBg;
 
$(document).ready(function(){

    //Dynamically wrap image
    var tagTarget = '<div id="tag-wrapper"';
    if(isCreator) {
        tagTarget += ' class="iscreator"';
    }
    tagTarget += '></div>';
    $(".taggable").wrap(tagTarget);
    
    //Dynamically size wrapper div based on image dimensions
   // $("#tag-wrapper").css({width: $("img").outerWidth(), height: $("img").outerHeight()});
    $("#tag-wrapper").css({width: imgwidth, height: imgheight});
    
    //Append #tag-target content and #tag-input content
    $("#tag-wrapper").append('<div id="tag-target"></div><div id="tag-input"><label for="tag-name">Term:</label><input type="text" id="tag-name"><button type="submit">Submit</button>&nbsp;<button type="reset">Cancel</button></div>');
    
    if(isGame) {
        $(window).keypress(function(e) {
    	    if(e.keyCode == 13){
    	        log("pressed enter key");
    	        onInputSubmit();
    	        e.preventDefault();
	        }
        });
        $("#ans").submit(onInputSubmit);
        defaultBg = $("#ans").css("background-color");
        $("#playAgainButton").click(startGame);
    }
    
    if(!isCreator) {
        return;
    }
    
    //$("#tag-wrapper").click(function(e){
    $(".taggable").click(function(e){     
        //Determine area within element that mouse was clicked
        mouseX = e.pageX - $("#tag-wrapper").offset().left;
        mouseY = e.pageY - $("#tag-wrapper").offset().top;
        
        //Get height and width of #tag-target
        targetWidth = $("#tag-target").outerWidth();
        targetHeight = $("#tag-target").outerHeight();
        
        //Determine position for #tag-target
        targetX = mouseX-targetWidth/2;
        targetY = mouseY-targetHeight/2;
        
        //Determine position for #tag-input
        inputX = mouseX+targetWidth/2;
        inputY = mouseY-targetHeight/2;
        
        //Animate if second click, else position and fade in for first click
        if($("#tag-target").css("display")=="block")
        {
            $("#tag-target").animate({left: targetX, top: targetY}, 500);
            $("#tag-input").animate({left: inputX, top: inputY}, 500);
        } else {
            $("#tag-target").css({left: targetX, top: targetY}).fadeIn();
            $("#tag-input").css({left: inputX, top: inputY}).fadeIn();
        }
        
        //Give input focus
        $("#tag-name").focus(); 
    });
    
    //If cancel button is clicked
    $('button[type="reset"]').click(function(){
        closeTagInput();
    });
    
    //If enter button is clicked within #tag-input
    $("#tag-name").keyup(function(e) {
        if(e.keyCode == 13) submitTag();
    }); 
    
    //If submit button is clicked
    $('button[type="submit"]').click(function(){
        submitTag();
    });
 
});
 
/******** START GAME SPECIFIC CODE */

function startGame() { // called from HTML
    correctIdxs = [];
    $("#playAgain").hide(200);
    updateStatusBar();
    showMessage("Type the tagged item and press enter.");
    showNextQA();
    return !1;
}

function showNextQA() {
    // enable controls
    controlsDisabled = false;
    // reset input
    $("#ans").val("");
    $("#ans").focus();
    $("#ans").css("background-color", defaultBg);
    // get next term and show corresponding tag
    curQ = genRandomNotIn(correctIdxs, tags.length-1);
    showTag(tags[curQ][1]);
}

// when enter key is hit
function onInputSubmit() {
    if(controlsDisabled) {
        return;
    }
    hideMessage();
    var newBg = "#faa";
    var correct = "yes";
    if(tags[curQ][0] == $("#ans").val()) {
        newBg = "#afa";
        correctIdxs.push(curQ);
    }
    else {
        correct = "no";
        // TODO make them type correct answer
    }
    updateStatusBar();
    // AJAX record stats
    $.ajax(ajaxStatsPath, {
        data:{'term':tags[curQ][2], 'correct':correct}
    });
    
    //TODO score change
    $("#ans").animate({
        backgroundColor:newBg
    }, 150, function() {
        log("wait 500 ms");
        setTimeout("onAnswerIndicatorFinish()", 500);
    });
}

//When the correct/incorrect answer indicator finishes animating
function onAnswerIndicatorFinish() {
    log("onAnswerIndicatorFinish");
    hideTag(tags[curQ][1]);
    if(!isWin()) {
        controlsDisabled = false;
        showNextQA();
    } 
}

// update the game status bar
function updateStatusBar() {
    log("updateStatusBar");
    var percentDone = correctIdxs.length/tags.length;
    var newWidth = $("#statusbar").width() * percentDone;
    $("#currentstatus").animate({
        width:newWidth+"px"
    }, 250);
}

// show the given message
function showMessage(messageString) {
    log("showMessage:"+messageString);
    $("#messagetext").html(messageString);
    $("#messages").slideDown(300);
}

function hideMessage(){
    $("#messages").slideUp(300);
}

// check if user has won
function isWin(){
    log("isWin");
    if(correctIdxs.length >= tags.length){
        log("DONE");
        /*// stop timer
        clearInterval(timer);
        
        // update best time if necessary
        if(timerSeconds < besttime) {
            besttime = timerSeconds;
        }*/
        
        // show game done and play again
        showMessage("Game Done");
        $("#playAgain").show(200);
        
        //disable controls
        controlsDisabled = true;
        return true;
    }
    return false;
}

/******** END GAME SPECIFIC CODE */

// submit a new tag (AJAX)
function submitTag()
{
    curTagContent = $("#tag-name").val();
    if(curTagContent.trim() == "") {
        alert('Please enter some text.');
        return !1;
    }
    $.ajax({ url:ajaxCreatePath,
        data:{'term':curTagContent,
        'setid':setId,
        'xpos':targetX,
        'ypos':targetY
    }, success: function(tagId) {
        addTag(curTagContent, tagCounter, targetX, targetY, tagId);
    }});
    closeTagInput();
}

// add tag to HTML
function addTag(content, htmlId, xpos, ypos, tagId) {
    //Adds a new hotspot to image
    var hotspot = '<div id="hotspot-' + htmlId + '" class="hotspot'
    if(isGame) {
        hotspot += ' hideborder';
    }
    hotspot += '" style="left:' + xpos + 'px; top:' + ypos + 'px;">';
    if(!isGame) {
        hotspot += '<span>' + content + '</span>';
    }
    hotspot += '</div>';
    $("#tag-wrapper").append(hotspot);
    $(".removeWhenTagged").html("");
    tagCounter++;
    
    if(isGame) {
        tags.push([content,htmlId,tagId]);
        return;
    }
    
    //Adds a new list item below image. Also adds events inline since they are dynamically created after page load
    var newTag = '<p id="hotspot-item-' + htmlId + '" class="atag" onmouseover="showTag('+htmlId+')" onmouseout="hideTag(' + htmlId + ')">' + content;
    if(isCreator) {
        newTag += '<span class="remove" onclick="removeTag('+htmlId+',\''+tagId+'\')">Remove</span>';
    }
    newTag += '</p>';
    $("#tag-wrapper").after(newTag);
}
 
function closeTagInput()
{
    $("#tag-target").fadeOut();
    $("#tag-input").fadeOut();
    $("#tag-name").val("");
}

// remove tag using AJAX
function removeTag(htmlId,tagId)
{

    $("#hotspot-item-"+htmlId).fadeOut();
    $("#hotspot-"+htmlId).fadeOut();
    $.ajax({ url:ajaxDeletePath,
        data:{'tagid':tagId}});
}
 
function showTag(i)
{
    $("#hotspot-"+i).addClass("hotspothover");
}
 
function hideTag(i)
{
    $("#hotspot-"+i).removeClass("hotspothover");
}