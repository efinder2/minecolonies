package com.minecolonies.coremod.colony.jobs;

import net.minecraft.resources.ResourceLocation;
import com.minecolonies.api.client.render.modeltype.ModModelTypes;
import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.api.colony.jobs.ModJobs;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.coremod.entity.ai.citizen.herders.EntityAIWorkRabbitHerder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The rabbit herder job
 */
public class JobRabbitHerder extends AbstractJob<EntityAIWorkRabbitHerder, JobRabbitHerder>
{
    /**
     * Instantiates the placeholder job.
     *
     * @param entity the entity.
     */
    public JobRabbitHerder(final ICitizenData entity)
    {
        super(entity);
    }

    /**
     * Generate your AI class to register.
     *
     * @return your personal AI instance.
     */
    @Nullable
    @Override
    public EntityAIWorkRabbitHerder generateAI()
    {
        return new EntityAIWorkRabbitHerder(this);
    }

    /**
     * Get the RenderBipedCitizen.Model to use when the Citizen performs this job role.
     *
     * @return Model of the citizen.
     */
    @NotNull
    @Override
    public ResourceLocation getModel()
    {
        return ModModelTypes.RABBIT_HERDER_ID;
    }
}
