'use strict';

import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import { fetchGetEffect } from './api/apiCall'
import { LoginLink, LogoutLink, RegisterLink } from './Links'
import { GET_LOGGED_IN_USER } from './api/apiEndpoints'
import { UWSUser } from './api/entities'
import MailSettingsPane from './components/MailSettingsPane'
import SourcesPane from './components/SourcesPane'
import GroupPane from './components/GroupPane'
import { getPermissionClosure } from './api/entityFunctions';

const getNav = (user: UWSUser) => {
	const common = (<>
		<Link to="/index">Home</Link>
		<Link to="/sources">Sources</Link>
		<Link to="/groups">Groups</Link>
	</>);
	if (user === null) {
		return (
			<nav>
				<LoginLink />
				<RegisterLink />
				{common}
			</nav>);
	} else {
		return (
			<nav>
				<LogoutLink />
				{common}
				<Link to="/settings">Mail settings</Link>
			</nav>);
	}
}

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
		return (<>
			<Router>
				{getNav(user)}
				<Routes>
					<Route path="/index" element={<h1>Unified Webcomic Subscriber</h1>} />
					<Route path="/sources" element={<SourcesPane user={userPerms} />} />
					<Route path="/groups" element={<GroupPane user={userPerms} />} />
					{user !== null &&
						<Route path="/settings" element={<MailSettingsPane initMailSettings={user.mailSettings} />} />}
				</Routes>
			</Router>
		</>);
	}
}

export default UserArea;