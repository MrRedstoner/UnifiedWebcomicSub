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
					setError(null);
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

const asyncFetchGet = async (endpoint: string, data: Record<string, string>, setItem: (item: Object) => void, setError: (error: any) => void, setIsLoaded: (loaded: boolean) => void) => {
	endpoint += '?' + (new URLSearchParams(data)).toString();
	const response = await fetch(endpoint, {
		method: "GET",
		headers: new Headers({
			'Accept': 'application/json'
		}),
	});
	if (!response.ok) {
		const err = await response.json();
		setError(err.message);
		setIsLoaded(true);
	} else {
		const data = await response.json();
		setItem(data);
		setIsLoaded(true);
	}
}

const asyncFetchPost = async (endpoint: string, data: Object, setItem: (item: Object) => void, setError: (error: any) => void, setIsLoaded: (loaded: boolean) => void) => {
	const response = await fetch(endpoint, {
		method: "POST",
		headers: new Headers({
			'Content-Type': 'application/json',
			'Accept': 'application/json'
		}),
		body: JSON.stringify(data)
	});
	if (!response.ok) {
		const err = await response.json();
		setError(err.message);
		setIsLoaded(true);
	} else {
		const data = await response.json();
		setItem(data);
		setIsLoaded(true);
	}
}

export { fetchGetEffect, asyncFetchPost, asyncFetchGet };