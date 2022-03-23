'use strict';

import React from 'react';

const LoginLink = () => {
	return (
		<a href="/login">Login</a>
	);
}

const LogoutLink = () => {
	return (
		<a href="/logout">Logout</a>
	);
}

const RegisterLink = () => {
	return (
		<a href="/registration">Register</a>
	);
}

export { LoginLink, LogoutLink, RegisterLink };
