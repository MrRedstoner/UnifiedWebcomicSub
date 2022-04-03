'use strict';

import React, { useState } from 'react'
import SourceList from './SourceList'
import { UserPermissionClosure } from '../api/entities';
import SourceCreate from './SourceCreate';
import SourceDetail from './SourceDetail';

type Props = {
	user: UserPermissionClosure;
}

type DetailOpen = {
	id: number;
}

const SourcesPane: React.FC<Props> = ({ user }) => {
	const [detail, setDetail] = useState<DetailOpen>(null);

	const setDetailId = (id: number) => {
		setDetail({ id: id });
	}

	const onNew = () => {
		setDetail({ id: null });
	}

	const newBtn = <button disabled={!user.createSource} onClick={onNew}>New</button>

	if (detail == null) {
		return (
			<div>
				<h2>Sources</h2>
				{newBtn}
				<SourceList onRow={setDetailId} />
			</div>);
	} else {
		if (detail.id == null) {
			return (
				<div>
					<h2>Sources</h2>
					{newBtn}
					<SourceList onRow={setDetailId} />
					<SourceCreate user={user} onCreated={setDetailId} />
				</div>);
		} else {
			return (
				<div>
					<h2>Sources</h2>
					{newBtn}
					<SourceList onRow={setDetailId} />
					<SourceDetail id={detail.id} user={user} />
				</div>);
		}
	}
};

export default SourcesPane;