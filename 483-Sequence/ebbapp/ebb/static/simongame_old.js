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

// user's typed/clicked answer
var answerText = "";
var lives; var numQsAnswered;

// ready for scoping
var isMultChoice = false;
var simonSays = true;
var itsOppositeDay = false;
var mirrorCup = false;
var wrongQAIdx = null;


$(document).ready(function(){
    log("document ready");
    
    onMultChoiceReady();

    // start new game when play again button clicked
    $("#playAgainButton").click(function() {
        log("play again click");
        startNewGame();
        return !1;
    });
    
    // start the first game
    parseData();
    startNewGame();
});

function onMultChoiceReady() {
    // get the default background color
    defaultBg = $(".answer").css("background-color");

    $(".answer").click(function() {
        log("answer click detected");
        onAnswerSubmit("#"+$(this).attr("id"));
        controlsDisabled = true;
        return !1;
    });
}

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
    
    numQsAnswered=0;
    lives = 3;
    
    timerSeconds = 10;
    $("#timer").text(timerSeconds);
    
    // hide the Play Again button and bonus
    $("#playAgain").hide(200);
    $("#bonus").hide(0);
    

	showMessage("Click the right answer to start timer");
    
    showNextQA();
    
    itsOppositeDay = false;
	mirrorCup = false;
}

// parse JSON data in textarea
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

function onAnswerSubmit(answerId) {
    log("onAnswerSubmit:"+answerId);
    if(alreadyAnswered || controlsDisabled) {
        // answered or controls disabled (end of game), so ignore
        log("answered:"+alreadyAnswered+" or disabled:"+controlsDisabled);
        return !1;
    }
    
    // start the timer if it hasn't started yet
    if(!timerStarted) {
        log("starting timer");
        timerStarted = true;
        // update every tenth of a second
        timerSeconds = 10;
        timer = setInterval("updateTimer()",100);
        
        if (!simonSays) {
        	hideMessage();
        }
    }
    // TODO: change to make this work on images using $(id).hasClass("img")
    
     $(".smile").hide(0);
    
    // figure out amount to change score by and new background color
    var scoreChange = 0;
    var newBg, isCorrect;
    
    var check2 = data[correctQAIdx].answer;
    
    answerText = $(answerId).text();
    
    if(itsOppositeDay) {
    	check2 = data[wrongQAIdx].answer;
    }
    if (mirrorCup) {
    	check2 = check2.split("").reverse().join("");
    }
    numQsAnswered ++;
    
    log("answer:"+answerText+"\tcorrect:"+data[correctQAIdx].answer);
    var correct = "yes";
    if(answerText.toLowerCase() == check2.toLowerCase()) {
        // correct
        log("correct");
        scoreChange += 1+scoreBonus;
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
        correct="no";
        lives --;
    }
    changeScore(scoreChange);
    if(isCorrect){
        scoreBonus = (scoreBonus+1)* 2;
    }
    
    // AJAX record stats
    $.ajax(ajaxStatsPath, {
        data:{'term':data[correctQAIdx].termId, 'correct':correct}
    });
    
    // THIS WILL HIGHLIGHT THE WRONG ANSWER FOR NOW
    // momentarily fade the color to indicate right/wrong answer
    $(answerId).animate({
        backgroundColor:newBg
    }, 150, function() {
        log("wait 500 ms");
        setTimeout("onAnswerIndicatorFinish()", 500);
    });
}

//When the correct/incorrect answer indicator finishes animating
function onAnswerIndicatorFinish() {
    log("onAnswerIndicatorFinish");
    if(!isWin()) {
        controlsDisabled = false;
        showNextQA();
    }
    //reset bg color
    $(".answer").css("background-color",defaultBg);

}

// show the next question-answers
function showNextQA() {
    log("showNextQA");
    alreadyAnswered = false;
    
    // update the prompt with a random prompt
    correctQAIdx = genRandom(data.length-1);
    var curQA = data[correctQAIdx];
    if(curQA.type == "text"){
        $("#prompt").text(curQA.prompt);
        $("#prompt").removeClass("image");
        $("#prompt").addClass("text");
    }
    else {
        // TODO: change src
        $("#prompt").html("<img height='180' src='/static/"+curQA.prompt+"'>");
        $("#prompt").removeClass("text");
        $("#prompt").addClass("image");
    }

    wrongQAIdx = genRandomNotIn([correctQAIdx],data.length-1);
    var randomizedQAs = shuffle([correctQAIdx, wrongQAIdx]);

	var answer0 = data[randomizedQAs[0]].answer;
	var answer1 = data[randomizedQAs[1]].answer;
		    	
    if (simonSays && timerStarted) {
    	makeSimonChoose();
        if (mirrorCup) {
        	answer0 = answer0.split("").reverse().join("");
        	answer1 = answer1.split("").reverse().join("");
    	}
    	timerSeconds = 10 - (numQsAnswered* 0.5); // RESET - TODO better
    }

    // TODO: more generic N number of questions
    $("#answerNumber0").text(answer0);
    $("#answerNumber1").text(answer1);

}

// RIGHTAHERE
// make Simon choose what the user should do
function makeSimonChoose() {
	var choice = (genRandom(1));	//2

	if (choice == 0){
		showMessage("Choose the RIGHT answer");		
		itsOppositeDay = false;
		$("#messages").css("background-color","#1589FF");
	}
	else //if (choice == 1)
	{
		showMessage("Choose the WRONG answer");
		itsOppositeDay = true;
		$("#messages").css("background-color","#C11B17");
	}
	/*else {
		showMessage("Don't touch anything!");
		stopSign = true;
	}*/
	
	if (genRandom(6) == 0) {
		// reverse string
		mirrorCup = true;
		$("#bonus").fadeIn(150).delay(800).html("Mirror mode!").fadeOut(150);
	}
	else // normal
	{
		mirrorCup = false;
	}

	/*if (genRandom(6) == 0){
		// timing
		timeItRight = true;
	}
	else {
		timeItRight = false;
	}*/
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
    timerSeconds = Math.round((timerSeconds-0.1)*10)/10;
    if(timerSeconds == Math.round(timerSeconds)) {
        // add a '.0'
        $("#timer").text(timerSeconds+".0");
    }
    else {
        $("#timer").text(timerSeconds);
    }
    
    if (simonSays && timerSeconds == 0)
    {
    	//stop timer
    	clearInterval(timer);
    	
    	//user ran out of time
    	ranOutofTime();
    }
}

function ranOutofTime() {
	//notify user that they ran out of time
	$("#bonus").css("background-color","#C11B17");		//RIGHTAHERE RED COLOR WON'T WORK
	$("#bonus").fadeIn(150).delay(1000).html("TIME'S UP!").fadeOut(150,onComplete);
	lives --;
}

function onComplete() {
	$("#bonus").css("background-color","rgba(0, 0, 0, 0.6)");
	
	//count it as a wrong answer
	// incorrect
	var scoreChange = 0;
	log("incorrect");
	scoreChange -= 1;
	scoreBonus = 0;
	newBg = "#faa";
	isCorrect = false;
	previousCorrect = false;
	// meh smiley face
	$(".smile").hide(0);
	$("#smile0").show(0);

    
	//restart timer at 10 seconds
	if(!isWin()){
	timerSeconds = 10;
	timer = setInterval("updateTimer()",100);
	
	//show next question
	showNextQA();
	}
}

// check if user has won
function isWin(){
    log("isWin");
    if(lives < 0) {
        log("DONE");
        // stop timer
        clearInterval(timer);
        
        // update best time if necessary
        if(timerSeconds < besttime) {
            besttime = timerSeconds;
        }
        
        if (simonSays) {
	        $("#messages").css("background-color","#aaa");
        }
        
        // show game done and play again
        showMessage("Game Done | High score: "+highscore+" |  Best time: "+besttime);
        $("#playAgain").show(200);
        return true;
    }
    return false;
}
