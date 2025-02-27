// JSON data for questions/answers
var data;

// user's score
var score;

// how many bonus points to add to score
var scoreBonus;

// current question and answer
var curQA;

// correct question and answer index
var correctQAIdx;

// array of the correct questions and answers
var correctQAIdxs;

// default background color of answers
var defaultBg;

// boolean, keep user from answering during background fade
var alreadyAnswered;

// boolean, whether or not arrow keys for game control are disabled
var controlsDisabled;

// boolean, previous answer is correct 
var previousCorrect;

// boolean, whether or not game timer has started
var timerStarted;

// seconds used for current game
var timerSeconds;

// the timer used to tracking seconds
var timer;

// highscore starts at 0
var highscore = 0;

// best time starts at positive infinity
var besttime = Infinity;

// user's typed answer to the prompt
var typedAns = "";

$(document).ready(function(){
    log("document ready");
    // get the default background color
    defaultBg = $("#ans").css("background-color");
    log(defaultBg);
    
    $(window).keypress(function(e) {
	  if(e.keyCode == 13){
				typedAns = $("#ans").val();
				log("You typed: " + typedAns);
				e.preventDefault();
				
			    // start the timer if it hasn't started yet
				if(!timerStarted) {
					log("starting timer");
					timerStarted = true;
					// update every tenth of a second
					timer = setInterval("updateTimer()",100);
					
					hideMessage();
				}
				
				$(".smile").hide(0);
				
				// figure out amount to change score by and new background color
				var scoreChange = 0;
				var newBg, isCorrect;
				
				if(typedAns == data[correctQAIdx].answer) {
					// correct
					log("correct");
					scoreChange += 1+scoreBonus;
					correctQAIdxs.push(correctQAIdx);
					newBg = "#afa";
					if(previousCorrect && genRandom(4) == 4) {
						// random extra bonus
						$("#smile2").show(0);
					}
					else{
						// regular smiley face
						$("#smile1").show(0);
					}
					isCorrect = true;
					previousCorrect = true;
					updateStatusBar();
				}
				else {
					// incorrect
					log("incorrect");
					scoreChange -= 1;
					scoreBonus = 0;
					newBg = "#faa";
					isCorrect = false;
					previousCorrect = false;
					// meh smiley face
					$("#smile0").show(0);
				}
		
				changeScore(scoreChange);
				if(isCorrect){
					scoreBonus = (scoreBonus+1)* 2;
				}
		// momentarily fade the color to indicate right/wrong answer
		$("#ans").animate({
			backgroundColor:newBg
			}, 150, function() {
				log("wait 500 ms");
				setTimeout("onAnswerIndicatorFinish()", 500);
			});
				
		return !1;
	  }	  
    });
    
  
/*    $(".answer").click(function() {
        log("answer click detected");
        onClickAnswer("#"+$(this).attr("id"));
        controlsDisabled = true;
        return !1;
    });*/
    
    // start new game when play again button clicked
    $("#playAgainButton").click(function() {
        log("play again click");
        startNewGame();
        return !1;
    });
    
    // temporarily disable arrow key game functionality to allow editing
    $("#data").focus(function () {
        controlsDisabled = true;
    }).focusout(function () {
        controlsDisabled = false;
    });
    
    // submit new JSON data for the game
    $("#submitData").click(function() {
        parseData();
        startNewGame();
        return !1;
    });
    
    // start the first game
    parseData();
    startNewGame();
});

// initialize (or reset) the game
function startNewGame(){
    log("startNewGame");
    controlsDisabled = false;
    previousCorrect = false;
    timerStarted = false;
    
    //reset score, bonus, and timer seconds
    score = 0;
    scoreBonus = 0;
    $("#score").text(score); 
    timerSeconds = 0;
    $("#timer").text(timerSeconds);
    
    // hide the Play Again button and bonus
    $("#playAgain").hide(200);
    $("#bonus").hide(0);
    
    // remove any messages
    showMessage("Type the answer and press 'Enter' to start timer");
    
    // reset correct answer idxs
    correctQAIdxs = new Array();
    
    // reset status bar
    updateStatusBar();
    
    showNextQA();
}

// parse JSON data in textarea
function parseData() {
    log("parseData");
    try {
        data = jQuery.parseJSON($('#data').val()); 
        // preload any images (TODO link)
        for(var i=0;i<data.length;i++){
            if(data[i].type == "image"){
                $("<img src='PlayZam_files/"+data[i].prompt+"' alt='"+data[i].prompt+"'>").appendTo("#imagedumpster");
            }
        }
    } catch(e) {
        alert("Invalid JSON");
    }
}

//When the correct/incorrect answer indicator finishes animating
function onAnswerIndicatorFinish() {
    log("onAnswerIndicatorFinish");
    $("#ans").css("background-color",defaultBg);
    if(!isWin()) {
        controlsDisabled = false;
        showNextQA();
    }
    //reset value of textbox
    $("#ans").val("");
}

// show the next question-answers
function showNextQA() {
    log("showNextQA");
    alreadyAnswered = false;
    
    // update the prompt with a random prompt
    correctQAIdx = genRandomNotIn(correctQAIdxs,data.length-1);
    var curQA = data[correctQAIdx];
    if(curQA.type == "text"){
        $("#prompt").text(curQA.prompt);
        $("#prompt").removeClass("image");
        $("#prompt").addClass("text");
    }
    else {
        // TODO: change src
        $("#prompt").html("<img height='180' src='PlayZam_files/"+curQA.prompt+"'>");
        $("#prompt").removeClass("text");
        $("#prompt").addClass("image");
    }

    var wrongQAIdx = genRandomNotIn([correctQAIdx],data.length-1);
    var randomizedQAs = shuffle([correctQAIdx, wrongQAIdx]);
    
    // TODO: more generic N number of questions
    $("#answerNumber0").text(data[randomizedQAs[0]].answer);
    $("#answerNumber1").text(data[randomizedQAs[1]].answer);
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

// show a bonus with the given string, operator and amount
// e.g. showBonus("combo!","+","3")
function showBonus(string, operator, amount) {
    $("#bonus").fadeIn(300).delay(500).html(string+"<br><span class='bigger'>"+operator+amount+"</span>").fadeOut(150);
}

// handle score change
function changeScore(amount) {
    log("changeScore:"+amount);
    var curScore = parseInt($("#score").text());
    if ((curScore+amount)<0){
        curScore=0;
    }
    else {
        curScore += amount;
    }
    $("#score").text(curScore);
    
    // update highscore if necessary
    if(curScore > highscore) {
        highscore = curScore;
    }
    
    // if there's a bonus, show it in the messages
    if(scoreBonus > 0) {
        showBonus("Combo","+",scoreBonus);
    }
}

// update the timer seconds by adding 1/10 second
function updateTimer() {
    // round to nearest 10th (precision problem)
    timerSeconds = Math.round((timerSeconds+0.1)*10)/10;
    if(timerSeconds == Math.round(timerSeconds)) {
        // add a '.0'
        $("#timer").text(timerSeconds+".0");
    }
    else {
        $("#timer").text(timerSeconds);
    }
}

// update the game status bar
function updateStatusBar() {
    log("updateStatusBar");
    var percentDone = correctQAIdxs.length/data.length;
    var newWidth = $("#statusbar").width() * percentDone;
    $("#currentstatus").animate({
        width:newWidth+"px"
    }, 250);
}

// check if user has won
function isWin(){
    log("isWin");
    if(correctQAIdxs.length >= data.length){
        log("DONE");
        // stop timer
        clearInterval(timer);
        
        // update best time if necessary
        if(timerSeconds < besttime) {
            besttime = timerSeconds;
        }
        
        // show game done and play again
        showMessage("Game Done | High score: "+highscore+" |  Best time: "+besttime);
        $("#playAgain").show(200);
        return true;
    }
    return false;
}

function log(message) {
console.log(message);
}
