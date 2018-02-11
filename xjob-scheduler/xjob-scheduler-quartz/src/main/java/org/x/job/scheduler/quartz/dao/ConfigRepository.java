package org.x.job.scheduler.quartz.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.x.job.scheduler.quartz.entity.Config;

@Repository
public interface ConfigRepository extends JpaRepository<Long, Config> {

}
