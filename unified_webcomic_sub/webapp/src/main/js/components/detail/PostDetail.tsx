'use strict';

import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Post, PostOption, UserPermissionClosure } from '../../api/entities';
import { POST_SERVICE_READ_DETAIL, POST_SERVICE_VOTE } from '../../api/apiEndpoints';
import { asyncFetchGet, asyncFetchPost } from '../../api/apiCall';

type RowProps = {
    id: number;
    content: string;
    votes: number;
    onClick: (id: number) => void;
}

const PostOptionRow: React.FC<RowProps> = ({ id, content, votes, onClick }) => {
    return <div>
        {content}
        <button onClick={() => onClick(id)}>Vote</button>
        {votes} votes
    </div>;
}

type OptProps = {
    opts: PostOption[];
    onVote: (id: number) => void;
}

const PostOptions: React.FC<OptProps> = ({ opts, onVote }) => {
    if (opts.length === 0) {
        return <></>;
    }
    //TODO display if voted already
    const lines = opts.map(opt => <PostOptionRow key={opt.id} id={opt.id} content={opt.content} onClick={onVote} votes={opt.voteCount}/>);
    return (<div>
        {lines}
    </div>);
}

type Props = {
    user: UserPermissionClosure;
}

const PostDetail: React.FC<Props> = ({ user }) => {
    const navigate = useNavigate();
    const { id } = useParams();
    const onBack = () => { navigate("/posts"); };

    const [error, setError] = useState(null);
    const [isLoaded, setIsLoaded] = useState(false);
    const [post, setPost] = useState<Post>(null);

    const loadData = async () => {
        setIsLoaded(false);
        setError(null);
        const data = { id: id.toString() };
        await asyncFetchGet(POST_SERVICE_READ_DETAIL, data, setPost, setError, setIsLoaded);
    }

    const onVote = async(id: number) => {
        if(user.registered){
            //TODO update vote counts?
            const noop=()=>{};
            await asyncFetchPost(POST_SERVICE_VOTE, id, noop, noop, noop);
        } else {
            window.alert("Register or log in to vote");
        }
    }

    useEffect(() => {
        loadData().catch(console.error);
    }, [id]);

    if (error) {
        return (<>
            <button onClick={onBack}>Back to list</button>
            <p>Error: {error}</p>
        </>);
    } else if (!isLoaded) {
        return (<>
            <button onClick={onBack}>Back to list</button>
            <div>Loading...</div>
        </>);
    } else {
        return (<>
            <button onClick={onBack}>Back to list</button>
            <h3>{post.title}</h3>
            <p>{post.content}</p>
            <PostOptions opts={post.options} onVote={onVote} />
        </>);
    }
}

export default PostDetail;