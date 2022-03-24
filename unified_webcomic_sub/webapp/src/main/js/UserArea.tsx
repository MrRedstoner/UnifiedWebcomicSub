import React, { useState, useEffect } from 'react';
import fetchGetEffect from './api/apiCall'
import { LoginLink, LogoutLink, RegisterLink } from './Links'
import { GET_LOGGED_IN_USER } from './api/apiEndpoints'

type UWSUser = {
	name: string;
};
//TODO proper name display, mail setting saving

const UserArea: React.FC = () => {
	const [error, setError] = useState(null);
	const [isLoaded, setIsLoaded] = useState(false);
	const [user, setUser] = useState<UWSUser>(null);

	useEffect(fetchGetEffect(GET_LOGGED_IN_USER, setUser, setError, setIsLoaded), [])

	if (error) {
		return <div>Error: {error.message}</div>;
	} else if (!isLoaded) {
		return <div>Loading...</div>;
	} else if (user == null) {
		return (
			<>
				<LoginLink />
				<RegisterLink />
			</>
		)
	} else {
		return (
			<>
				<LogoutLink />
				<div>Hello {user.name}</div>
			</>
		)
	}
}

export default UserArea;