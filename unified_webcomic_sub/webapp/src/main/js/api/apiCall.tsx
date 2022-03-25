'use strict';

function fetchGetEffect(endpoint: string, setItem: (item: Object) => void, setError: (error: any) => void, setIsLoaded: (loaded: boolean) => void) {
	return () => {
		fetch(endpoint)
			.then(response => {
				if (!response.ok) {
					return response.json().then(err => { throw new Error(err.message) })
				}
				else {
					return response.json();
				}
			})
			.then(
				(result) => {
					setIsLoaded(true);
					setItem(result);
				}
			)
			.catch(
				(error) => {
					setIsLoaded(true);
					setError(error);
				}
			)
	}
}

//TODO make an async function that just makes the call
function fetchPostHandler(endpoint: string, setItem: (item: Object) => void, setError: (error: any) => void, setIsLoaded: (loaded: boolean) => void){
	return (body:Object) => {
		fetch(endpoint, {method:"POST", headers: new Headers({'Content-Type': 'application/json','Accept':'application/json'}),body: JSON.stringify(body)})
			.then(response => {
				if (!response.ok) {
					return response.json().then(err => { throw new Error(err.message) })
				}
				else {
					console.log("no issue")
					return response.json();
				}
			})
			.then(
				(result) => {
					console.log("setting result");
					setItem(result);
					setIsLoaded(true);
				}
			)
			.catch(
				(error) => {
					setError(error);
					setIsLoaded(true);
				}
			)
	}
}

const asyncFetchPost = async(endpoint: string, data:Object, setItem: (item: Object) => void, setError: (error: any) => void, setIsLoaded: (loaded: boolean) => void) => {
	const response=await fetch(endpoint, {method:"POST", headers: new Headers({'Content-Type': 'application/json','Accept':'application/json'}),body: JSON.stringify(data)});
	if(!response.ok){
		const err=await response.json();
		setError(err.message);
		setIsLoaded(true);
	}else{
		const data=await response.json();
		setItem(data);
		setIsLoaded(true);
	}
}

export {fetchGetEffect, asyncFetchPost};