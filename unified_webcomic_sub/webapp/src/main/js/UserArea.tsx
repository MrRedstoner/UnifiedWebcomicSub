import React, { useState, useEffect } from 'react';
import { fetchGetEffect, asyncFetchPost } from './api/apiCall'
import { LoginLink, LogoutLink, RegisterLink } from './Links'
import { GET_LOGGED_IN_USER, SET_MAIL_SETTINGS } from './api/apiEndpoints'
import { MailSettings } from './api/entities'

type UWSUser = {
	name: string;
	mailSettings: MailSettings;
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
			</>)
	} else {
		return (
			<>
				<LogoutLink />
				<h1>Hello {user.name}</h1>
				<UserBtn initMailSettings={user.mailSettings} />
			</>)
	}
}

type Props = {
	initMailSettings: MailSettings;
};

const UserBtn: React.FC<Props> = ({ initMailSettings }) => {
	const [error, setError] = useState(null);
	const [isLoaded, setIsLoaded] = useState(true);
	const [mailSettings, setMailSettings] = useState<MailSettings>(initMailSettings);

	const newval = !mailSettings.daily;

	const onClick = async () => {
		setIsLoaded(false);
		setError(null);
		asyncFetchPost(SET_MAIL_SETTINGS, { daily: newval }, setMailSettings, setError, setIsLoaded);
	};
	const btn = <button onClick={onClick}>Switch daily</button>;

	if (error) {
		return (
			<>
				<div>Error: {error}</div>
				{btn}
			</>);
	} else if (!isLoaded) {
		return (
			<>
				<div>Loading...</div>
				{btn}
			</>);
	} else {
		return (
			<>
				<div>Result: {JSON.stringify(mailSettings)}</div>
				{btn}
			</>);
	}
}

export default UserArea;