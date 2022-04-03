'use strict';

import React, { useState, useEffect } from 'react';
import { fetchGetEffect } from './api/apiCall'
import { LoginLink, LogoutLink, RegisterLink } from './Links'
import { GET_LOGGED_IN_USER } from './api/apiEndpoints'
import { UWSUser } from './api/entities'
import MailSettingsPane from './components/MailSettingsPane'
import SourcesPane from './components/SourcesPane'
import GroupPane from './components/GroupPane'
import { getPermissionClosure } from './api/entityFunctions';

const UserArea: React.FC = () => {
	const [error, setError] = useState(null);
	const [isLoaded, setIsLoaded] = useState(false);
	const [user, setUser] = useState<UWSUser>(null);

	useEffect(fetchGetEffect(GET_LOGGED_IN_USER, setUser, setError, setIsLoaded), [])

	if (error) {
		return <div>Error: {error.message}</div>;
	} else if (!isLoaded) {
		return <div>Loading...</div>;
	} else {
		const userPerms = getPermissionClosure(user);
		if (user == null) {
			return (
				<>
					<LoginLink />
					<RegisterLink />
					<SourcesPane user={userPerms} />
					<GroupPane user={userPerms} />
				</>)
		} else {
			return (
				<>
					<LogoutLink />
					<h1>Hello {user.name}</h1>
					<MailSettingsPane initMailSettings={user.mailSettings} />
					<SourcesPane user={userPerms} />
					<GroupPane user={userPerms} />
				</>)
		}
	}
}

export default UserArea;