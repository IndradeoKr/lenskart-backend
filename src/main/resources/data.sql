-- Drop the old unique constraint on cart_id that was created when the relationship was @OneToOne.
-- Hibernate ddl-auto=update does not remove old constraints, so we must do it manually.
ALTER TABLE orders DROP CONSTRAINT IF EXISTS uks1sr8a1rkx80gwq9pl0952dar;
ALTER TABLE cart_product DROP CONSTRAINT IF EXISTS ukhrphk67tkykrvenhskm5uqgg8;
