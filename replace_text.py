import os
import re

replacements = {
    # Feight -> Freight
    'feightTemplateId': 'freightTemplateId',
    'FeightTemplateId': 'FreightTemplateId',
    'feight_template': 'freight_template',
    'FeightTemplate': 'FreightTemplate',
    'feightTemplate': 'freightTemplate',
    'PmsFeightTemplate': 'PmsFreightTemplate',
    'pmsFeightTemplate': 'pmsFreightTemplate',
    
    # Weight/Fee
    'firstWeight': 'baseWeight',
    'FirstWeight': 'BaseWeight',
    'first_weight': 'base_weight',
    'firstFee': 'baseShippingFee',
    'FirstFee': 'BaseShippingFee',
    'first_fee': 'base_shipping_fee',
    'continueWeight': 'incrementalWeightUnit',
    'ContinueWeight': 'IncrementalWeightUnit',
    'continue_weight': 'incremental_weight_unit',
    'continmeFee': 'incrementalFee',
    'ContinmeFee': 'IncrementalFee',
    'continme_fee': 'incremental_fee',
    
    # Points/Growth
    'historyIntegration': 'lifetimeRewardPoints',
    'HistoryIntegration': 'LifetimeRewardPoints',
    'history_integration': 'lifetime_reward_points',
    'luckeyCount': 'bonusDrawsRemaining',
    'LuckeyCount': 'BonusDrawsRemaining',
    'luckey_count': 'bonus_draws_remaining',
    'freeFreightPoint': 'freeShippingThreshold',
    'FreeFreightPoint': 'FreeShippingThreshold',
    'free_freight_point': 'free_shipping_threshold',
    'commentGrowthPoint': 'reviewRewardXp',
    'CommentGrowthPoint': 'ReviewRewardXp',
    'comment_growth_point': 'review_reward_xp',
    
    # Privileges
    'priviledgeMemberPrice': 'hasVipPricing',
    'PriviledgeMemberPrice': 'HasVipPricing',
    'priviledge_member_price': 'has_vip_pricing',
    'priviledgeFreeFreight': 'hasFreeShippingPerk',
    'PriviledgeFreeFreight': 'HasFreeShippingPerk',
    'priviledge_free_freight': 'has_free_shipping_perk',
    'priviledgeSignIn': 'canEarnLoginRewards',
    'PriviledgeSignIn': 'CanEarnLoginRewards',
    'priviledge_sign_in': 'can_earn_login_rewards',
    'priviledgeComment': 'hasReviewPrivilege',
    'PriviledgeComment': 'HasReviewPrivilege',
    'priviledge_comment': 'has_review_privilege',
    'priviledgePromotion': 'hasPromotionPrivilege',
    'PriviledgePromotion': 'HasPromotionPrivilege',
    'priviledge_promotion': 'has_promotion_privilege',
    'priviledgeBirthday': 'hasBirthdayPrivilege',
    'PriviledgeBirthday': 'HasBirthdayPrivilege',
    'priviledge_birthday': 'has_birthday_privilege',

    # Verify
    'vertifyMan': 'reviewerName',
    'VertifyMan': 'ReviewerName',
    'vertify_man': 'reviewer_name',
    'vertifyStatus': 'approvalStatus',
    'VertifyStatus': 'ApprovalStatus',
    'vertify_status': 'approval_status',
    'VertifyRecord': 'VerifyRecord',
    'vertifyRecord': 'verifyRecord',
    'vertify_record': 'verify_record',

    # Replay
    'commentReplay': 'commentReply',
    'CommentReplay': 'CommentReply',
    'comment_replay': 'comment_reply',
    'replayCount': 'replyCount',
    'ReplayCount': 'ReplyCount',
    'replay_count': 'reply_count',

    # Collects
    'collectCouont': 'likesCount',
    'CollectCouont': 'LikesCount',
    'collect_couont': 'likes_count',
    'collectProductCount': 'savedProductsCount',
    'CollectProductCount': 'SavedProductsCount',
    'collect_product_count': 'saved_products_count',
    'collectSubjectCount': 'savedArticlesCount',
    'CollectSubjectCount': 'SavedArticlesCount',
    'collect_subject_count': 'saved_articles_count',
    'collectTopicCount': 'savedTopicsCount',
    'CollectTopicCount': 'SavedTopicsCount',
    'collect_topic_count': 'saved_topics_count',
    'collectCommentCount': 'likedCommentsCount',
    'CollectCommentCount': 'LikedCommentsCount',
    'collect_comment_count': 'liked_comments_count',

    # Prefrence
    'prefrence': 'preference',
    'Prefrence': 'Preference',
    'PREFRENCE': 'PREFERENCE',
    
    # Intergration
    'intergration': 'rewardPoints',
    'Intergration': 'RewardPoints',
    
    # Growth & Integration specifically mapped to avoid breaking test names / imports
    'private Integer integration;': 'private Integer rewardPoints;',
    'getIntegration()': 'getRewardPoints()',
    'setIntegration(': 'setRewardPoints(',
    '".integration("': '".rewardPoints("',
    '"integration"': '"rewardPoints"',
    '@Column(name = "integration")': '@Column(name = "reward_points")',
    
    'private Integer growth;': 'private Integer experiencePoints;',
    'getGrowth()': 'getExperiencePoints()',
    'setGrowth(': 'setExperiencePoints(',
    '".growth("': '".experiencePoints("',
    '"growth"': '"experiencePoints"',
    '@Column(name = "growth")': '@Column(name = "experience_points")'
}

def process_directory(directory):
    for root, dirs, files in os.walk(directory):
        # skip git and node_modules
        if '.git' in root or 'node_modules' in root or 'target' in root:
            continue
        for file in files:
            if file.endswith(('.java', '.xml', '.ts', '.tsx', '.js', '.jsx', '.json', '.yml', '.yaml')):
                path = os.path.join(root, file)
                try:
                    with open(path, 'r', encoding='utf-8') as f:
                        content = f.read()
                        
                    new_content = content
                    for old, new in replacements.items():
                        new_content = new_content.replace(old, new)
                        
                    if new_content != content:
                        with open(path, 'w', encoding='utf-8') as f:
                            f.write(new_content)
                        print(f"Updated {path}")
                except Exception as e:
                    pass

process_directory('NexusCore')
process_directory('NexusFrontendWeb')
