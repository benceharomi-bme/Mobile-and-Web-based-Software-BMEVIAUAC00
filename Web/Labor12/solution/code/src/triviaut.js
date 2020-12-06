let remainingQuestions, totalQuestions, currentQuestion, correctAnswerIndex, numOfCorrectAnswers;
function getNextQuestion() {
  $("#start-game-form").hide()
  $("#game-section").show();
  currentQuestion++;
  const question = remainingQuestions.pop();
  if (question === undefined) {
    $("#game-section").hide()
    alert('Pontszám:' + numOfCorrectAnswers)
    $.get("start-game-form-contents.html")
    .then(html => {
      $("#start-game-form").html(html)
      $("#start-game-form").show()
      $("#start-game-form select[name='trivia_category']").removeAttr('disabled');
      $("#start-game-form select[name='trivia_difficulty']").removeAttr('disabled');
    });
    return;
  }
  correctAnswerIndex = Math.floor(Math.random() * 4);
  const questions = question.incorrect_answers.slice();
  questions.splice(correctAnswerIndex, 0, question.correct_answer);
  for(let i = 0; i < 4; i++){
    $(`.answer[data-answer-index='${i}'] .answer-text`).text(atob(questions[i])).
      click(e => {
        if(i !== correctAnswerIndex){
          $(`.answer .incorrect, #next-question`).show();
        } else {
          numOfCorrectAnswers++;
          $(`#next-question`).show();
        }
        $(`.answer[data-answer-index='${correctAnswerIndex}'] .correct`).show();
        $(".answer").attr("disabled", true);
      })
  }
  $("#current-question-number").text(currentQuestion);
  $("#question-category").text(atob(question.category));
  $("#question-text").text(atob(question.question));
  $(".answer").removeAttr('disabled')
  $(".answer .correct, .answer .incorrect, #next-question").hide();
  $("#next-question").click(e => {
    getNextQuestion();
  })
};

$(() => {
  $("#lets-play-button").on("click", () => {
      $("#lets-play-section, #start-game-form-section").toggle();
      $.get("start-game-form-contents.html")
        .then(html => {
          $("#start-game-form").html(html)
          $("#start-game-form select[name='trivia_category']").removeAttr('disabled');
          $("#start-game-form select[name='trivia_difficulty']").removeAttr('disabled');
        });
  });
});

$.get("start-game-form-contents.html").then(html => {
  $("#start-game-form").html(html).on("submit", e => {
    e.preventDefault();
    $("#start-game-form button[type='submit']").attr("disabled", true);
    $.get("https://opentdb.com/api.php?type=multiple&encode=base64&amount=" 
      + $("[name='trivia_amount']").val()
      + "&difficulty=" + $("[name='trivia_difficulty']").val()
      + "&category=" + $("[name='trivia_category']").val()
      ).then(data => {
        remainingQuestions = data.results;
        console.log(remainingQuestions);
        currentQuestion = 0;
        numOfCorrectAnswers = 0;
        $("#total-questions").text(remainingQuestions.length);
        $("#start-game-form button[type='submit']").removeAttr("disabled");
        getNextQuestion();
    });
  })
});