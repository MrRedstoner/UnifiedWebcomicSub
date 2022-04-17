'use strict';

import React from 'react';
import { useNavigate, useParams } from 'react-router-dom';

const PostDetail: React.FC = () => {
	const navigate = useNavigate();
	const { id } = useParams();
	const onBack = () => { navigate("/posts"); };
	return (<>
		<button onClick={onBack}>Back to list</button>
		<h2>PostDetail {id}</h2>
	</>);
}

export default PostDetail;