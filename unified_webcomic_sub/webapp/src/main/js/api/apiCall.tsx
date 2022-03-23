'use strict';

function fetchEffect(endpoint: string, setItem: (item: Object) => void, setError: (error: any) => void, setIsLoaded: (loaded: boolean) => void) {
	return () => {
		fetch(endpoint)
			.then(res => res.json())
			.then(
				(result) => {
					setIsLoaded(true);
					setItem(result);
				},
				(error) => {
					setIsLoaded(true);
					setError(error);
				}
			)
	}
}

export default fetchEffect