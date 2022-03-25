'use strict';

import React, { useState } from 'react';
import { MailSettings, MailSettingsChange } from '../api/entities'
import { SET_MAIL_SETTINGS } from '../api/apiEndpoints'
import { asyncFetchPost } from '../api/apiCall'
import CheckBox from './CheckBox'
import EmailBox from './EmailBox'
import SimpleSelect from './SimpleSelect'

type Props = {
	initMailSettings: MailSettings;
};

const DAYS_OF_WEEK=["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];

const MailSettingsPane: React.FC<Props> = ({ initMailSettings }) => {
	const [error, setError] = useState(null);
	const [isLoaded, setIsLoaded] = useState(true);
	const [mailSettings, setMailSettings] = useState<MailSettings>(initMailSettings);
	const [changes, setChanges] = useState<MailSettingsChange>({});

	const onClick = async () => {
		//console.log(changes);
		setIsLoaded(false);
		setError(null);
		await asyncFetchPost(SET_MAIL_SETTINGS, changes, setMailSettings, setError, setIsLoaded);
		setChanges({});
	};

	const onMail = (mail: string) => {
		const newChanges = Object.assign({}, changes);
		newChanges.mailAddress = mail;
		setChanges(newChanges);
	};

	const onDaily = (daily: boolean) => {
		const newChanges = Object.assign({}, changes);
		newChanges.daily = daily;
		setChanges(newChanges);
	};

	const onWeekly = (weekly: boolean) => {
		const newChanges = Object.assign({}, changes);
		newChanges.weekly = weekly;
		setChanges(newChanges);
	};

	const onDayOfWeek = (dow: number) => {
		const newChanges = Object.assign({}, changes);
		newChanges.dayOfWeek = dow;
		setChanges(newChanges);
	};

	const form = (
		<>
			<h2>Mail settings</h2>
			<EmailBox label="Email address" initialValue={mailSettings.mailAddress} setValue={onMail} />
			<br />
			<CheckBox label="Daily emails" initialValue={mailSettings.daily} setValue={onDaily} />
			<br />
			<CheckBox label="Weekly emails" initialValue={mailSettings.weekly} setValue={onWeekly} />
			<br />
			<SimpleSelect label="Day for weekly emails" initialValue={mailSettings.dayOfWeek} setValue={onDayOfWeek} valueList={DAYS_OF_WEEK}/>
			<br />
		</>);

	if (error) {
		return (
			<>
				{form}
				<div>Error: {error}</div>
				<button onClick={onClick}>Update</button>
			</>);
	} else if (!isLoaded) {
		return (
			<>
				{form}
				<button disabled={true}>Loading...</button>
			</>);
	} else {
		return (
			<>
				{form}
				<button onClick={onClick}>Update</button>
			</>);
	}
};

export default MailSettingsPane;