package com.syc.blog.service.feedback;

import com.syc.blog.feedback.FeedBack;
import com.syc.blog.mapper.feedback.FeedBackMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedBackService {
    @Autowired
    FeedBackMapper feedBackMapper;
    public int save(FeedBack feedBack) {
        return feedBackMapper.insert(feedBack);
    }
}
