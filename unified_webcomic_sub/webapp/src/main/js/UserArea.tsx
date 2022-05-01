'use strict';

import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import { fetchGetEffect } from './api/apiCall'
import { LoginLink, LogoutLink, RegisterLink } from './Links'
import { GET_LOGGED_IN_USER } from './api/apiEndpoints'
import { UWSUser, MailSettings } from './api/entities'
import MailSettingsPane from './components/MailSettingsPane'
import SourcesPane from './components/SourcesPane'
import GroupPane from './components/GroupPane'
import { getPermissionClosure } from './api/entityFunctions';
import PostsPane from './components/panes/PostsPane';
import HomePane from './components/panes/HomePane';
import ModeratorsPane from './components/panes/ModeratorsPane';
import UserRightsPane from './components/panes/UserRightsPane';

const getNav = (user: UWSUser) => {
	const common = (<>
		<Link to="/index">Home</Link>
		<Link to="/sources">Sources</Link>
		<Link to="/groups">Groups</Link>
		<Link to="/posts">Posts</Link>
		<Link to="/mods">Moderators</Link>
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
				<Link to="/settings">Mail settings</Link>
				<LogoutLink />
				{common}
				{(user.admin || user.owner) && 
					<Link to="/users">User rights</Link>}
			</nav>);
	}
}

const UserArea: React.FC = () => {
	const [error, setError] = useState(null);
	const [isLoaded, setIsLoaded] = useState(false);
	const [user, setUser] = useState<UWSUser>(null);

	useEffect(fetchGetEffect(GET_LOGGED_IN_USER, setUser, setError, setIsLoaded), [])

	const updateMailSettings = (ms: MailSettings) => {
		const newUser = Object.assign({}, user);
		newUser.mailSettings = ms;
		setUser(newUser);
	}

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
					<Route path="/index" element={<HomePane user={user} />} />
					<Route path="/sources" element={<SourcesPane user={userPerms} />} />
					<Route path="/groups" element={<GroupPane user={userPerms} />} />
					{user !== null &&
						<Route path="/settings" element={<MailSettingsPane initMailSettings={user.mailSettings} setMailSettings={updateMailSettings} />} />}
					<Route path="/posts/*" element={<PostsPane user={userPerms} />} />
					<Route path="/mods/*" element={<ModeratorsPane user={userPerms} />} />
					{user !== null && userPerms.admin &&
						<Route path="/users/*" element={<UserRightsPane user={userPerms} />} />}
				</Routes>
			</Router>
		</>);
	}
}

export default UserArea;