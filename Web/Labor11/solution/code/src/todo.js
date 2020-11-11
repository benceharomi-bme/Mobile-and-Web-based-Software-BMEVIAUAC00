function Todo(name, state) {
  this.name = name;
  this.state = state;
}

var todos = [];
todos = loadFromLocalStorage();
var states = ["active", "inactive", "done"];
var tabs = ["all"].concat(states);

var form = document.getElementById("new-todo-form");
var input = document.getElementById("new-todo-title");

form.onsubmit = function (event) {
    event.preventDefault(); // meggátoljuk az alapértelmezett működést, ami frissítené az oldalt
    if (input.value && input.value.length) { // ha érvényes érték van benne
        todos.push(new Todo(input.value, "active")); // új to-do-t aktív állapotban hozunk létre
        input.value = ""; // kiürítjük az inputot
        renderTodos();
    }
}

function Button(action, icon, type, title) {
  this.action = action; // a művelet, amit a gomb végez
  this.icon = icon; // a FontAwesome ikon neve (class="fas fa-*")
  this.type = type; // a gomb Bootstrapbeni típusa ("secondary", "danger" stb.)
  this.title = title; // a gomb tooltip szövege
}

var buttons = [ // a gombokat reprezentáló modell objektumok tömbje
  new Button("move-up", "arrow-up", "secondary", "Move up" ),
  new Button("move-down", "arrow-down", "secondary", "Move down" ),
  new Button("done", "check", "success", "Mark as done"),
  new Button("active", "plus", "secondary", "Mark as active"),
  // az objektumot dinamikusan is kezelhetjük, ekkor nem a konstruktorral példányosítjuk:
  { action: "inactive", icon: "minus", type: "secondary", title: "Mark as inactive" },
  new Button("remove", "trash", "danger", "Remove"),
];

function renderTodos() {
  saveToLocalStorage(todos);
  var todoList = document.getElementById("todo-list"); // megkeressük a konténert, ahová az elemeket tesszük
  todoList.innerHTML = ""; // a jelenleg a DOM-ban levő to-do elemeket töröljük
  var filtered = todos.filter(function(todo){ return todo.state === currentTab || currentTab === "all"; });
  filtered.forEach(function (todo) { // bejárjuk a jelenlegi todo elemeket (alternatív, funkcionális bejárással)
    var item = document.createElement("a"); // az elemet tároló <a>
    item.className = "list-group-item col";
    item.href = "#";
    item.innerHTML = todo.name;
    var buttonContainer = document.createElement("div"); // a gombok tárolója
    buttonContainer.className = "btn-group";
    buttons.forEach(function (button) { // a gomb modellek alapján legyártjuk a DOM gombokat
      if((button.action === "move-up" || button.action === "move-down") && currentTab !== "all"){
        return;
      }
      var btn = document.createElement("button"); // <button>
      btn.className = "btn btn-outline-" + button.type + " fas fa-" + button.icon;
      btn.title = button.title;
      if (todo.state === button.action) // azt a gombot letiljuk, amilyen állapotban van egy elem
          btn.disabled = true;
      if(todos.indexOf(todo) === 0 && button.action === "move-up"){
        btn.disabled = true;
      }
      if(todos.indexOf(todo) === todos.length-1 && button.action === "move-down"){
        btn.disabled = true;
      }
      btn.onclick = button.action === "remove"
        ? function () { // klikk eseményre megerősítés után eltávolítjuk a to-do-t
          if (confirm("Are you sure you want to delete the todo titled '" + todo.name + "'?")) {
              todos.splice(todos.indexOf(todo), 1); // kiveszünk a 'todo'-adik elemtől 1 elemet a todos tömbből
              renderTodos();
          }
        }
        : button.action === "move-up" ? _ => { 
          const index = todos.indexOf(todo);
          const element = todo;
          todos.splice(index, 1);
          todos.splice(index-1, 0, element);
          renderTodos();
        }
        : button.action === "move-down" ? _ => {
          const index = todos.indexOf(todo);
          const element = todo;
          todos.splice(index, 1);
          todos.splice(index+1, 0, element);
          renderTodos();
        }
        : function () { // klikk eseményre beállítjuk a to-do állapotát a gomb által reprezentált állapotra
          todo.state = button.action;
          renderTodos();
        }
      buttonContainer.appendChild(btn); // a <div>-be tesszük a gombot
    });
    var row = document.createElement("div"); // a külső konténer <div>, amibe összefogjuk az elemet és a műveletek gombjait
    row.className = "row";
    row.appendChild(item); // a sorhoz hozzáadjuk az <a>-t
    row.appendChild(buttonContainer); // és a gombokat tartalmazó <div>-et
    todoList.appendChild(row); // az összeállított HTML-t a DOM-ban levő #todo-list elemhez fűzzük
  });
  document.querySelector(".todo-tab[data-tab-name='all'] .badge").innerHTML = todos.length || "";

  for (var state of states)
    document.querySelector(".todo-tab[data-tab-name='" + state + "'] .badge").innerHTML = todos.filter(function (t){ return t.state === state; }).length || "";
}

renderTodos(); // kezdeti állapot kirajzolása

var currentTab; // a jelenleg kiválasztott fül

function selectTab(type) {
    currentTab = type; // eltároljuk a jelenlegi fül értéket
    for (var tab of document.getElementsByClassName("todo-tab")) {
        tab.classList.remove("active");// az összes fülről levesszük az .active osztályt
        if (tab.getAttribute("data-tab-name") == type)
            tab.classList.add("active");
    }

    renderTodos();
}

selectTab("all");

function saveToLocalStorage(object){
  const value = JSON.stringify(object);
  localStorage.setItem('todos-save' , value);
}

function loadFromLocalStorage(){
  const todosSave = localStorage.getItem('todos-save');
  return todos =JSON.parse(todosSave);
}