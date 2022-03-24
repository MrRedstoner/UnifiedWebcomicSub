'use strict';

import React, { useState, useEffect } from 'react';
import fetchGetEffect from './api/apiCall'
import {  GET_LOGGED_IN_USER } from './api/apiEndpoints'

type UWSUser = {
	name: string;
};
//TODO proper name display, mail setting saving

const UserNameDisplay: React.FC = () => {
	const [error, setError] = useState(null);
	const [isLoaded, setIsLoaded] = useState(false);
	const [items, setItems] = useState<UWSUser>(null);

	useEffect(fetchGetEffect(GET_LOGGED_IN_USER, setItems, setError, setIsLoaded), [])

	if (error) {
		return <div>Error: {error.message}</div>;
	} else if (!isLoaded) {
		return <div>Loading...</div>;
	} else {
		return (
			<div>{items?.name || "no user"}</div>
		);
	}
}

export default UserNameDisplay;