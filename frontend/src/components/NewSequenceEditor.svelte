<script lang="ts">
  import type { NumericSequenceDefinition } from "../model";
  import { createEventDispatcher } from "svelte";
  const dispatch = createEventDispatcher();

  let namespace = "";
  let name = "";
  let start = 1;

  function onClick() {
    dispatch("create", { name, namespace, start });
    console.log(start);
  }

  $: valid = namespace.length > 0 && name.length > 0 && start !== undefined && start !== null;
</script>

<div class="wrapper">
  <input type="text" bind:value={name} />
  @
  <input type="text" bind:value={namespace} />
  =
  <input type="number" bind:value={start} />
  <button on:click={onClick} disabled={!valid}>Create</button>
</div>

<style>
  .wrapper {
    padding: 1em;
    margin-top: 1em;
    background-color: #eee;
    border-radius: 3px;
  }
</style>
