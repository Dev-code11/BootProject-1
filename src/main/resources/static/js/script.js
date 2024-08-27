console.log("script is running")

let currentTheme=getTheme();
//initial
changeTheme();
//todo
function changeTheme(){
	console.log(currentTheme);
	
	//set to web page
	document.querySelector("html").classList.add(currentTheme); //html wali class k andar currentTheme add krdia
	
	const changeThemeButton = document.querySelector("#theme_button");
	changeThemeButton.querySelector("span").textContent = 
			currentTheme=="light" ? "dark" : "light" ;
	
	changeThemeButton.addEventListener("click", () => {
		
		const oldTheme=currentTheme;
		
		console.log("change theme button clicked");
		if(currentTheme==="dark"){
			//theme ko light
			currentTheme="light";
		} else{
			currentTheme="dark";
		}
		//local storage mein update
		setTheme(currentTheme);
		
		//remove the current theme
		document.querySelector("html").classList.remove(oldTheme);
		
		//set the current theme
		document.querySelector("html").classList.add(currentTheme);
		
		//change the text of button
		
		changeThemeButton.querySelector("span").textContent = 
		currentTheme=="light" ? "dark" : "light" ;
	});
	
	
}

//set theme to localstorage
function setTheme(theme){
	localStorage.setItem("theme",theme);
}

//get theme from localstorage
function getTheme(){
	let theme= localStorage.getItem("theme");
	return theme ? theme : "light";
}




