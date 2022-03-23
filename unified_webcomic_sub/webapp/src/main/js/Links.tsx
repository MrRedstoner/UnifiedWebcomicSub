'use strict';

import React from 'react';

const LoginLink: React.FC = () => {
	return (
		<a href="/login">Login</a>
	);
}

const LogoutLink: React.FC = () => {
	return (
		<a href="/logout">Logout</a>
	);
}

const RegisterLink: React.FC = () => {
	return (
		<a href="/registration">Register</a>
	);
}

export { LoginLink, LogoutLink, RegisterLink };
