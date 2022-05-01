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
					setItem(result);
					setIsLoaded(true);
					setError(null);
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

const asyncFetchGetState = async (endpoint: string, data: Record<string, string>, setItem: (item: Object) => void, setError: (error: any) => void) => {
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
	} else {
		const data = await response.json();
		setItem(data);
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

const asyncFetchPostState = async (endpoint: string, data: Record<string, string>, setItem: (item: Object) => void, setError: (error: any) => void) => {
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
	} else {
		const data = await response.json();
		setItem(data);
	}
}
export { fetchGetEffect, asyncFetchPost, asyncFetchGet, asyncFetchGetState, asyncFetchPostState };