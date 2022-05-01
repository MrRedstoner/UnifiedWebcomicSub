'use strict';

import React from 'react';
import { Routes, Route } from 'react-router-dom';
import ModeratorsList from '../list/ModeratorsList';
import ModeratorDetail from '../detail/ModeratorDetail';
import { UserPermissionClosure } from '../../api/entities';

type Props = {
	user: UserPermissionClosure;
}

const ModeratorsPane: React.FC<Props> = ({ user }) => {
	return (<><h2>Moderators</h2>
		<Routes>
			<Route path="show">
				<Route path=":id" element={<ModeratorDetail user={user} />} />
			</Route>
			<Route path="/" element={<ModeratorsList />} />
		</Routes>
	</>);

}

export default ModeratorsPane;